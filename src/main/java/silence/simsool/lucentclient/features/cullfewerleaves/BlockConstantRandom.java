package silence.simsool.lucentclient.features.cullfewerleaves;

import net.minecraft.core.BlockPos;

public class BlockConstantRandom {

	public static float getConstantRandomSeeded(BlockPos pos) {
		long seed = (pos.asLong() ^ 25214903917L) & 281474976710655L;
		seed = seed * 25214903917L + 11L & 281474976710655L;
		return ((int) (seed >> 48 - 24)) * 5.9604645E-8F;
	}

}