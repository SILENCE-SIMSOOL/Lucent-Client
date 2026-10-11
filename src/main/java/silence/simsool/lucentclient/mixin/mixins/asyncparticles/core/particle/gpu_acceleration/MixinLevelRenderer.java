package silence.simsool.lucentclient.mixin.mixins.asyncparticles.core.particle.gpu_acceleration;

import silence.simsool.lucentclient.features.asyncparticles.config.ComputeExecutionStage;
import silence.simsool.lucentclient.features.asyncparticles.config.ConfigHelper;
import silence.simsool.lucentclient.features.asyncparticles.core.particle.gpu_acceleration.GpuParticleBehavior;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
	@Inject(method = "render", at = @At("HEAD"))
	private void renderLevelHead(CallbackInfo ci) {
		if (ConfigHelper.isGpuParticles() && ConfigHelper.getComputeExecutionStage() == ComputeExecutionStage.LEVEL_RENDERING) {
			GpuParticleBehavior.getInstance().compute();
		}
	}
}