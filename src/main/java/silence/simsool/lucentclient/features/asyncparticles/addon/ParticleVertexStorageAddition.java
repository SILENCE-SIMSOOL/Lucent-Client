package silence.simsool.lucentclient.features.asyncparticles.addon;

import net.minecraft.client.renderer.state.level.QuadParticleRenderState;

public interface ParticleVertexStorageAddition {

	ParticleSlice asyncparticles$slice(int start, int end);

	@FunctionalInterface
	interface ParticleSlice {

		void forEachParticle(QuadParticleRenderState.ParticleConsumer consumer);

	}

}