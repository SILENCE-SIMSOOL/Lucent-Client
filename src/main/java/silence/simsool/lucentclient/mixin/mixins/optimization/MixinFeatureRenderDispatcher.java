package silence.simsool.lucentclient.mixin.mixins.optimization;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(FeatureRenderDispatcher.class)
public abstract class MixinFeatureRenderDispatcher {

	@Shadow
	@Final
	private MultiBufferSource.BufferSource bufferSource;

	@Inject(method = "renderAllFeatures", at = {
		@At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/NameTagFeatureRenderer;render(Lnet/minecraft/client/renderer/SubmitNodeCollection;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/gui/Font;)V"),
		@At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/ParticleFeatureRenderer;render(Lnet/minecraft/client/renderer/SubmitNodeCollection;)V")
	})
	private void drawBatch(CallbackInfo ci) {
		if (FastRenderMod.isEnabled() && FastRenderMod.EnhancedBatching) {
			this.bufferSource.endLastBatch();
		}
	}

}