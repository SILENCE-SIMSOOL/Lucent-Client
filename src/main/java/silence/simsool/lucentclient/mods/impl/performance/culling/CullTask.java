package silence.simsool.lucentclient.mods.impl.performance.culling;

import static silence.simsool.lucent.Lucent.mc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;
import silence.simsool.lucentclient.mods.impl.performance.culling.util.Vec3d;

public class CullTask implements Runnable {

	private final OcclusionCullingInstance culling;
	private volatile boolean running = true;
	public volatile boolean requestCull = false;
	private final AtomicBoolean isProcessing = new AtomicBoolean(false);

	private final Vec3d lastCameraPos = new Vec3d(0, 0, 0);
	private final Vec3d aabbMin = new Vec3d(0, 0, 0);
	private final Vec3d aabbMax = new Vec3d(0, 0, 0);

	private final List<Cullable> bufferA = new ArrayList<>(256);
	private final List<Cullable> bufferB = new ArrayList<>(256);
	private List<Cullable> writeBuffer = bufferA;
	private volatile List<Cullable> readBuffer = Collections.emptyList();

	private final List<Cullable> blockBufferA = new ArrayList<>(512);
	private final List<Cullable> blockBufferB = new ArrayList<>(512);
	private List<Cullable> blockWriteBuffer = blockBufferA;
	private volatile List<Cullable> blockReadBuffer = Collections.emptyList();

	private volatile Vec3 cameraMC = new Vec3(0, 0, 0);
	private int captureCounter = 0;
	private int lastPlayerChunkX = Integer.MIN_VALUE;
	private int lastPlayerChunkZ = Integer.MIN_VALUE;

	public CullTask(OcclusionCullingInstance culling) {
		this.culling = culling;
	}

	public synchronized boolean tryPopulateAndSwap(Iterable<Entity> entities, Vec3 camPos) {
		if (this.isProcessing.get()) {
			return false;
		}

		this.writeBuffer.clear();
		for (Entity entity : entities) {
			if (entity instanceof Cullable cullable) {
				cullable.setCullingBox(CullingHelper.getCullingBox(entity));
				cullable.setDistanceSq(entity.distanceToSqr(camPos));
				cullable.setShouldEntityAppearGlowing(mc.shouldEntityAppearGlowing(entity));
				this.writeBuffer.add(cullable);
			}
		}

		List<Cullable> ready = this.writeBuffer;
		this.writeBuffer = (this.writeBuffer == this.bufferA) ? this.bufferB : this.bufferA;
		this.readBuffer = ready;

		if (EntityCullingMod.CullBlockEntities && mc.level != null && mc.player != null) {
			int currentChunkX = mc.player.chunkPosition().x();
			int currentChunkZ = mc.player.chunkPosition().z();

			if (this.captureCounter++ % 5 == 0 || currentChunkX != this.lastPlayerChunkX || currentChunkZ != this.lastPlayerChunkZ) {
				this.lastPlayerChunkX = currentChunkX;
				this.lastPlayerChunkZ = currentChunkZ;
				populateBlockEntities(mc.level, currentChunkX, currentChunkZ);
			}
		} else if (!this.blockReadBuffer.isEmpty()) {
			this.blockReadBuffer = Collections.emptyList();
		}

		this.cameraMC = camPos;
		this.requestCull = true;
		this.isProcessing.set(true);
		return true;
	}

	private void populateBlockEntities(ClientLevel level, int playerChunkX, int playerChunkZ) {
		this.blockWriteBuffer.clear();
		for (int x = -8; x <= 8; x++) {
			for (int z = -8; z <= 8; z++) {
				if (!level.hasChunk(playerChunkX + x, playerChunkZ + z)) {
					continue;
				}
				LevelChunk chunk = level.getChunk(playerChunkX + x, playerChunkZ + z);
				if (chunk == null) {
					continue;
				}
				Map<BlockPos, BlockEntity> blockEntities = chunk.getBlockEntities();
				if (blockEntities.isEmpty()) {
					continue;
				}
				for (BlockEntity entity : blockEntities.values()) {
					if (entity instanceof Cullable cullable) {
						if (EntityCullingMod.isBlockEntityWhitelisted(entity) || mc.getBlockEntityRenderDispatcher().getRenderer(entity) == null) {
							continue;
						}
						cullable.setCullingBox(CullingHelper.setupAABB(entity, entity.getBlockPos()));
						cullable.setCullingBlockPos(entity.getBlockPos());
						this.blockWriteBuffer.add(cullable);
					}
				}
			}
		}

		List<Cullable> ready = this.blockWriteBuffer;
		this.blockWriteBuffer = (this.blockWriteBuffer == this.blockBufferA) ? this.blockBufferB : this.blockBufferA;
		this.blockReadBuffer = ready;
		EntityCullingMod.consideredBlockEntities = ready.size();
	}

	public void stop() {
		this.running = false;
	}

	@Override
	public void run() {
		while (this.running) {
			try {
				int delay = Math.max(1, EntityCullingMod.SleepDelay);
				Thread.sleep(delay);

				if (!EntityCullingMod.isEnabled() || mc.level == null || mc.player == null) {
					this.isProcessing.set(false);
					continue;
				}

				if (!this.isProcessing.get() && !this.requestCull) {
					continue;
				}

				Vec3 cam = this.cameraMC;
				this.requestCull = false;
				this.lastCameraPos.set(cam.x, cam.y, cam.z);

				this.culling.resetCache();
				cullEntities(cam, this.lastCameraPos);
				cullBlockEntities(cam, this.lastCameraPos);
			} catch (InterruptedException ignored) {
				break;
			} catch (Exception ignored) {
			} finally {
				this.isProcessing.set(false);
			}
		}
	}

	private void cullEntities(Vec3 cameraMC, Vec3d camera) {
		List<Cullable> list = this.readBuffer;
		if (list.isEmpty()) {
			return;
		}

		Entity cameraEntity = mc.getCameraEntity();
		int culledCount = 0;
		int tracingDist = EntityCullingMod.TracingDistance;
		double maxDistSq = (double) tracingDist * tracingDist;
		int hitboxLimit = EntityCullingMod.HitboxLimit;

		for (int i = 0; i < list.size(); i++) {
			Cullable cullable = list.get(i);
			if (cullable == null) {
				continue;
			}
			Entity entity = (Entity) cullable;

			if (entity == cameraEntity || (cameraEntity != null && entity.isPassengerOfSameVehicle(cameraEntity))) {
				cullable.setCulled(false);
				continue;
			}

			if (cullable.isShouldEntityAppearGlowing()) {
				cullable.setCulled(false);
				continue;
			}

			if (!EntityCullingMod.shouldCheckEntity(entity) || CullingHelper.ignoresCulling(entity)) {
				cullable.setCulled(false);
				continue;
			}

			if (cullable.isForcedVisible()) {
				continue;
			}

			double distSq = cullable.getDistanceSq();
//			if (distSq < 4.0) { // 카메라 아주 가까이 있는 엔티티는 컬링 제외
//				cullable.setCulled(false);
//				continue;
//			}

			if (distSq > maxDistSq) {
				cullable.setCulled(false);
				continue;
			}

			AABB box = cullable.getCullingBox();
			if (box == null || box.getXsize() > hitboxLimit || box.getYsize() > hitboxLimit || box.getZsize() > hitboxLimit) {
				cullable.setCulled(false);
				continue;
			}

			this.aabbMin.set(box.minX, box.minY, box.minZ);
			this.aabbMax.set(box.maxX, box.maxY, box.maxZ);

			boolean visible = this.culling.isAABBVisible(this.aabbMin, this.aabbMax, camera);
			cullable.setCulled(!visible);
			if (!visible) {
				culledCount++;
			}
		}

		EntityCullingMod.culledEntities = culledCount;
	}

	private void cullBlockEntities(Vec3 cameraMC, Vec3d camera) {
		if (!EntityCullingMod.CullBlockEntities) {
			return;
		}
		List<Cullable> list = this.blockReadBuffer;
		if (list.isEmpty()) {
			return;
		}

		int hitboxLimit = EntityCullingMod.HitboxLimit;
		Iterator<Cullable> iterator = list.iterator();
		while (iterator.hasNext()) {
			Cullable cullable;
			try {
				cullable = iterator.next();
			} catch (NullPointerException | ConcurrentModificationException ex) {
				break;
			}
			if (cullable == null) {
				break;
			}
			if (cullable.isForcedVisible()) {
				continue;
			}
			BlockPos pos = cullable.getCullingBlockPos();
			if (pos != null && closerThan(pos, cameraMC, 64.0)) {
				AABB box = cullable.getCullingBox();
				if (box == null || box.getXsize() > hitboxLimit || box.getYsize() > hitboxLimit || box.getZsize() > hitboxLimit) {
					cullable.setCulled(false);
					continue;
				}
				this.aabbMin.set(box.minX, box.minY, box.minZ);
				this.aabbMax.set(box.maxX, box.maxY, box.maxZ);
				boolean visible = this.culling.isAABBVisible(this.aabbMin, this.aabbMax, camera);
				cullable.setCulled(!visible);
			}
		}
	}

	private static boolean closerThan(BlockPos blockPos, Vec3 position, double d) {
		double dx = (double) blockPos.getX() + 0.5D - position.x;
		double dy = (double) blockPos.getY() + 0.5D - position.y;
		double dz = (double) blockPos.getZ() + 0.5D - position.z;
		return (dx * dx + dy * dy + dz * dz) < d * d;
	}

}