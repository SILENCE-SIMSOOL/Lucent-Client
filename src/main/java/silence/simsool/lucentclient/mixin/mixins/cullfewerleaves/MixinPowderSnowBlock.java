package silence.simsool.lucentclient.mixin.mixins.cullfewerleaves;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import silence.simsool.lucentclient.features.cullfewerleaves.CullFewerLeavesHelper;
import silence.simsool.lucentclient.features.cullfewerleaves.Cullable;

@Mixin(PowderSnowBlock.class)
public abstract class MixinPowderSnowBlock implements Cullable {

	@Override
	public boolean lucent$shouldCullSide(BlockState state, BlockGetter view, BlockPos pos, Direction facing) {
		return CullFewerLeavesHelper.shouldCullSide(
			1,
			view,
			pos,
			facing,
			block -> block instanceof PowderSnowBlock
		);
	}

}