package silence.simsool.lucentclient.mixin.mixins.asyncparticles.core.particle.gpu_acceleration;

import silence.simsool.lucentclient.features.asyncparticles.config.ConfigHelper;
import silence.simsool.lucentclient.features.asyncparticles.core.particle.gpu_acceleration.GpuParticleBehavior;
import silence.simsool.lucentclient.features.asyncparticles.core.particle.gpu_acceleration.GpuParticleGroupRenderer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.state.ParticlesRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticlesRenderState.class)
public class MixinParticlesRenderState {

	@Inject(method = "submit", at = @At("RETURN"))
	private void onSubmit(SubmitNodeStorage submitNodeStorage, CameraRenderState camera, CallbackInfo ci) {
		if (ConfigHelper.isGpuParticles() && !GpuParticleBehavior.getInstance().getOrCreateRenderer().isShouldSkip()) {
			submitNodeStorage.submitParticleGroup(GpuParticleGroupRenderer.getInstance());
		}
	}

}