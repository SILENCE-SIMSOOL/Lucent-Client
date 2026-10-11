package silence.simsool.lucentclient.features.asyncparticles.addon;

import net.minecraft.client.particle.QuadParticleGroup;

public interface GpuQuadParticleRenderState {
	GpuParticleGroup asyncparticles$gpuParticleGroup();

	void asyncparticles$setGroup(QuadParticleGroup quadParticleGroup);
}