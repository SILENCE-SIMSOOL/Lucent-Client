package silence.simsool.lucentclient.mods.impl.performance.culling;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CullingDataProvider implements DataProvider {

	private ClientLevel level = null;
	private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
	private int lastChunkX = Integer.MIN_VALUE;
	private int lastChunkZ = Integer.MIN_VALUE;
	private boolean lastChunkLoaded = false;

	@Override
	public boolean prepareChunk(int chunkX, int chunkZ) {
		this.level = mc.level;
		if (this.level == null) {
			this.lastChunkLoaded = false;
			return false;
		}
		if (chunkX == this.lastChunkX && chunkZ == this.lastChunkZ) {
			return this.lastChunkLoaded;
		}
		this.lastChunkX = chunkX;
		this.lastChunkZ = chunkZ;
		this.lastChunkLoaded = this.level.hasChunk(chunkX, chunkZ);
		return this.lastChunkLoaded;
	}

	@Override
	public boolean isOpaqueFullCube(int x, int y, int z) {
		if (this.level == null || !this.lastChunkLoaded) {
			return false;
		}
		if (x < -30000000 || x > 30000000 || z < -30000000 || z > 30000000) {
			return false;
		}
		if (y < this.level.getMinY() || y >= this.level.getMaxY()) {
			return false;
		}

		this.mutablePos.set(x, y, z);
		BlockState state = this.level.getBlockState(this.mutablePos);
		return state.isSolidRender();
	}

	@Override
	public void cleanup() {
		this.level = null;
		this.lastChunkX = Integer.MIN_VALUE;
		this.lastChunkZ = Integer.MIN_VALUE;
		this.lastChunkLoaded = false;
	}

}