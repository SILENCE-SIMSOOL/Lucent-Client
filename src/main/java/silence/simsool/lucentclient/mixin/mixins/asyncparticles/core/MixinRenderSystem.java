package silence.simsool.lucentclient.mixin.mixins.asyncparticles.core;

import com.mojang.blaze3d.systems.RenderSystem;
import silence.simsool.lucentclient.features.asyncparticles.core.backend.Backends;
import silence.simsool.lucentclient.features.asyncparticles.coremod.AsyncParticlesMixinPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class MixinRenderSystem {

	@Inject(method = "initRenderer", at = @At("RETURN"))
	private static void onInitRenderer(CallbackInfo ci) {
		Backends.init();
	}

	@Inject(method = "initRenderer", remap = false, order = 9000, at = @At("RETURN"))
	private static void onInitRendererLate(CallbackInfo ci) {
		AsyncParticlesMixinPlugin.LOGGER
			.info("AsyncParticles will cancel some of its mixins to achieve specific functions, so you can ignore the related Mixinsquared-canceller warnings below.");
	}

}