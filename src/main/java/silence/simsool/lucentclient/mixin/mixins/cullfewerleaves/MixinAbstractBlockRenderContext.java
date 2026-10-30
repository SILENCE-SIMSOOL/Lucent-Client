package silence.simsool.lucentclient.mixin.mixins.cullfewerleaves;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import silence.simsool.lucentclient.features.cullfewerleaves.Cullable;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext")
public abstract class MixinAbstractBlockRenderContext {

	@Shadow
	protected BlockState state;

	@Shadow
	protected BlockPos pos;

	@Shadow
	protected BlockAndTintGetter level;

	@ModifyExpressionValue(
		method = "shouldDrawSide",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;skipRendering(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"
		)
	)
	private boolean onShouldDrawSide(boolean isSideInvisible, Direction facing) {
		if (this.state != null && this.state.getBlock() instanceof Cullable cullable) {
			return isSideInvisible || cullable.lucent$shouldCullSide(this.state, this.level, this.pos, facing);
		}
		return isSideInvisible;
	}

}