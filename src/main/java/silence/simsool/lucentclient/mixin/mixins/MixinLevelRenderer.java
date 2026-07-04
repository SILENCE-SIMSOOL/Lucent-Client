package silence.simsool.lucentclient.mixin.mixins;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

//	@Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
//	private void onRenderHitOutline(PoseStack poseStack, VertexConsumer builder, Entity entity, double camX, double camY, double camZ, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
//		if (BlockOverlayMod.isEnabled()) {
//			ci.cancel();
//		}
//	}

//	@Inject(method = "submitBlockOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/LevelRenderState;)V", at = @At("HEAD"), cancellable = true)
//	private void onSubmitBlockOutline(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LevelRenderState levelRenderState, CallbackInfo ci) {
//		if (BlockOverlayMod.isEnabled()) { 
//			ci.cancel();
//		}
//	}

}