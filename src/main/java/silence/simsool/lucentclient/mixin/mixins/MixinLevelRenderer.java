package silence.simsool.lucentclient.mixin.mixins;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

//	@Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
//	private void onRenderHitOutline(PoseStack poseStack, VertexConsumer builder, double camX, double camY, double camZ, BlockOutlineRenderState state, int color, float width, CallbackInfo ci) {
//		if (BlockOverlayMod.isEnabled()) {
//			ci.cancel();
//		}
//	}

//	@Inject(method = "renderBlockOutline(Lnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/PoseStack;ZLnet/minecraft/client/renderer/LevelRenderState;)V", at = @At("HEAD"), cancellable = true)
//	private void onRenderBlockOutline(MultiBufferSource bufferSource, PoseStack poseStack, boolean bl, Object levelRenderState, CallbackInfo ci) {
//		if (BlockOverlayMod.isEnabled()) {
//			ci.cancel();
//		}
//	}

}