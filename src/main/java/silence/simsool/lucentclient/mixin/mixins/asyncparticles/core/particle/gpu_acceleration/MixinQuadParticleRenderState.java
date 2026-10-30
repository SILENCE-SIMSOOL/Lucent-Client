package silence.simsool.lucentclient.mixin.mixins.asyncparticles.core.particle.gpu_acceleration;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.particle.QuadParticleGroup;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import silence.simsool.lucentclient.features.asyncparticles.addon.GpuParticleGroup;
import silence.simsool.lucentclient.features.asyncparticles.addon.GpuQuadParticleRenderState;

@Mixin(QuadParticleRenderState.class)
public class MixinQuadParticleRenderState implements GpuQuadParticleRenderState {

	@Unique
	private GpuParticleGroup asyncparticles$gpuParticleGroup;

//	@Definition(id = "particleCount", field = "Lnet/minecraft/client/renderer/state/QuadParticleRenderState;particleCount:I")
//	@Expression("this.particleCount > 0")
//	@ModifyExpressionValue(method = "submit", at = @At("MIXINEXTRAS:EXPRESSION"))
//	private boolean submit(boolean original) {
//		return original || !asyncparticles$gpuParticleGroup.asyncparticles$getGpuParticles().isEmpty();
//	}

	@Override
	public GpuParticleGroup asyncparticles$gpuParticleGroup() {
		return asyncparticles$gpuParticleGroup;
	}

	@Override
	public void asyncparticles$setGroup(QuadParticleGroup quadParticleGroup) {
		this.asyncparticles$gpuParticleGroup = (GpuParticleGroup) quadParticleGroup;
	}

}