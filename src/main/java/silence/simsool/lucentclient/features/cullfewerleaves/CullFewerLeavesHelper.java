package silence.simsool.lucentclient.features.cullfewerleaves;

import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CullFewerLeavesHelper {

	public static final int DEPTH = 2;
	public static final float REJECTION_CHANCE = 0.0f;

	public static boolean shouldCullSide(int depth, BlockGetter view, BlockPos pos, Direction facing, Function<Block, Boolean> blockCheck) {
		Vec3i vec = facing.getUnitVec3i();
		boolean cull = true;
		boolean outerBlock = false;

		for (int i = 1; i <= depth; i++) {
			BlockState state = view.getBlockState(pos.offset(vec.multiply(i)));
			cull &= state != null && blockCheck.apply(state.getBlock());

			if (!cull && i == 1) {
				outerBlock = true;
			}
		}

		if (!outerBlock && !cull && BlockConstantRandom.getConstantRandomSeeded(pos) <= REJECTION_CHANCE) {
			cull = true;
		}

		return cull;
	}
}