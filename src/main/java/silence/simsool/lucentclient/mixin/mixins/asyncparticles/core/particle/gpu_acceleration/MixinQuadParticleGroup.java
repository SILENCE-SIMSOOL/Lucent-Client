package silence.simsool.lucentclient.mixin.mixins.asyncparticles.core.particle.gpu_acceleration;

import silence.simsool.lucentclient.features.asyncparticles.addon.GpuParticleGroup;
import silence.simsool.lucentclient.features.asyncparticles.addon.GpuQuadParticleRenderState;
import silence.simsool.lucentclient.features.asyncparticles.core.particle.ParticleHelper;
import silence.simsool.lucentclient.features.asyncparticles.core.particle.tick.AsyncTickBehavior;
import net.minecraft.client.particle.QuadParticleGroup;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(QuadParticleGroup.class)
public abstract class MixinQuadParticleGroup implements GpuParticleGroup {

	@Shadow
	@Final
	private QuadParticleRenderState particleTypeRenderState;
	@Unique
	private final Queue<SingleQuadParticle> asyncparticles$gpuParticles = ParticleHelper.newParticleQueue();

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(CallbackInfo ci) {
		((GpuQuadParticleRenderState) particleTypeRenderState).asyncparticles$setGroup((QuadParticleGroup) (Object) this);
	}

	@Override
	public Queue<SingleQuadParticle> asyncparticles$getGpuParticles() {
		return asyncparticles$gpuParticles;
	}

	@Override
	public void asyncparticles$removeDeadGpuParticles() {
		AsyncTickBehavior.getInstance().doParticlesRemoveIf(asyncparticles$gpuParticles);
	}

}