package silence.simsool.lucentclient.mods.impl.performance.culling;

import static silence.simsool.lucent.Lucent.mc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;
import silence.simsool.lucentclient.mods.impl.performance.culling.util.Vec3d;

public class CullTask implements Runnable {

	private final OcclusionCullingInstance culling;
	private volatile boolean running = true;
	public volatile boolean requestCull = false;

	private final Vec3d lastCameraPos = new Vec3d(0, 0, 0);
	private final Vec3d aabbMin = new Vec3d(0, 0, 0);
	private final Vec3d aabbMax = new Vec3d(0, 0, 0);

	private final List<Cullable> snapshotA = new ArrayList<>(256);
	private final List<Cullable> snapshotB = new ArrayList<>(256);
	private volatile List<Cullable> activeSnapshot = Collections.emptyList();
	private boolean useBufferA = true;

	private volatile Vec3 cameraMC = new Vec3(0, 0, 0);

	public CullTask(OcclusionCullingInstance culling) {
		this.culling = culling;
	}

	public synchronized void populateAndSwap(Iterable<Entity> entities, Vec3 camPos) {
		List<Cullable> target = this.useBufferA ? this.snapshotA : this.snapshotB;
		target.clear();

		for (Entity entity : entities) {
			if (entity instanceof Cullable cullable) {
				cullable.setCullingBox(entity.getBoundingBox());
				cullable.setDistanceSq(entity.distanceToSqr(camPos));
				target.add(cullable);
			}
		}

		this.activeSnapshot = target;
		this.useBufferA = !this.useBufferA;
		this.cameraMC = camPos;
		this.requestCull = true;
	}

	public void stop() {
		this.running = false;
	}

	@Override
	public void run() {
		while (this.running) {
			try {
				Thread.sleep(10);

				if (!EntityCullingMod.isEnabled() || mc.level == null || mc.player == null) {
					continue;
				}

				Vec3 cam = this.cameraMC;
				if (this.requestCull || cam.x != this.lastCameraPos.x || cam.y != this.lastCameraPos.y || cam.z != this.lastCameraPos.z) {
					this.requestCull = false;
					this.lastCameraPos.set(cam.x, cam.y, cam.z);

					this.culling.resetCache();
					cullEntities(cam, this.lastCameraPos);
				}
			} catch (InterruptedException ignored) {
				break;
			} catch (Exception ignored) {
			}
		}
	}

	private void cullEntities(Vec3 cameraMC, Vec3d camera) {
		List<Cullable> list = this.activeSnapshot;
		if (list.isEmpty()) {
			return;
		}

		Entity cameraEntity = mc.getCameraEntity();
		int culledCount = 0;

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

			if (mc.shouldEntityAppearGlowing(entity)) {
				cullable.setCulled(false);
				continue;
			}

			if (!EntityCullingMod.shouldCheckEntity(entity)) {
				cullable.setCulled(false);
				continue;
			}

			double distSq = cullable.getDistanceSq();
			if (distSq < 64.0) {
				cullable.setCulled(false);
				continue;
			}

			if (distSq > 128.0 * 128.0) {
				cullable.setCulled(false);
				continue;
			}

			AABB box = cullable.getCullingBox();
			if (box == null || box.getXsize() > 15.0 || box.getYsize() > 15.0 || box.getZsize() > 15.0) {
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

}