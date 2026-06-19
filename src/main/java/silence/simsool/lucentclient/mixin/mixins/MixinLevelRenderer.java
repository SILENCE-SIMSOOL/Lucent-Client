package silence.simsool.lucentclient.mixin.mixins;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import silence.simsool.lucentclient.mods.impl.graphics.BlockOverlayMod;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

	@Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
	private void onRenderHitOutline(PoseStack poseStack, VertexConsumer builder, double camX, double camY, double camZ, BlockOutlineRenderState state, int color, float width, CallbackInfo ci) {
		if (BlockOverlayMod.isEnabled()) {
			ci.cancel();
		}
	}

}