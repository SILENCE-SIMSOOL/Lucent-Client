package silence.simsool.lucentclient.features.asyncparticles.addon;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.particle.Particle;

@ApiStatus.Internal
public interface ParticleAddon {

	void asyncparticles$setTicked();

	void asyncparticles$resetTicked();

	boolean asyncparticles$isTicked();

	void asyncparticles$setGpuLightGot();

	boolean asyncparticles$isFirstGpuLightGet();

	@SuppressWarnings({"unchecked", "rawtypes"})
	default <T extends Particle> Class<T> asyncparticles$getRealClass() {
		return (Class) this.getClass();
	}

	byte getTickFlag();

}