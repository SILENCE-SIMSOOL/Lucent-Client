package silence.simsool.lucentclient.mixin.mixins;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import silence.simsool.lucentclient.mods.impl.graphics.BlockOverlayMod;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

	@Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
	private void onRenderHitOutline(PoseStack poseStack, VertexConsumer builder, Entity entity, double camX, double camY, double camZ, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
		if (BlockOverlayMod.isEnabled()) {
			ci.cancel();
		}
	}

}