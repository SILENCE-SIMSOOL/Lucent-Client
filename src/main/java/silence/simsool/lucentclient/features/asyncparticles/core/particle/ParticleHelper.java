package silence.simsool.lucentclient.features.asyncparticles.core.particle;

import silence.simsool.lucentclient.features.asyncparticles.addon.LightCachedParticleAddon;
import silence.simsool.lucentclient.features.asyncparticles.config.AsyncParticlesConfig;
import silence.simsool.lucentclient.features.asyncparticles.config.ConfigHelper;
import silence.simsool.lucentclient.features.asyncparticles.core.particle.gpu_acceleration.GpuParticleBehavior;
import silence.simsool.lucentclient.features.asyncparticles.core.particle.tick.AsyncTickBehavior;
import silence.simsool.lucentclient.features.asyncparticles.util.BusyWaitEvictingQueue;
import silence.simsool.lucentclient.features.asyncparticles.util.IterationSafeEvictingQueue;
import silence.simsool.lucentclient.features.asyncparticles.util.ParticleThreadLocal;
import silence.simsool.lucentclient.features.asyncparticles.util.ThreadUtil;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.world.level.chunk.MissingPaletteEntryException;

import java.util.Queue;

public class ParticleHelper {

	public static final ParticleThreadLocal<Integer> DESTRUCTION_LIGHT_CACHE = new ParticleThreadLocal<>(ThreadUtil::isOnMainThread);
	public static final ParticleThreadLocal<Boolean> CULL_UNDERWATER_PARTICLE_TYPE = ParticleThreadLocal.withInitial(ThreadUtil::isOnMainThread, () -> false);

	public static <T extends Particle> Queue<T> newParticleQueue() {
		return newParticleQueue(16);
	}

	public static <T extends Particle> Queue<T> newParticleQueue(int initSize) {
		return IterationSafeEvictingQueue.newInstance(
			initSize,
			AsyncParticlesConfig.MAX_PARTICLE_LIMIT);
	}

	public static<T extends Particle> Queue<T> newBusyWaitParticleQueue() {
		return newBusyWaitParticleQueue(16);
	}

	public static<T extends Particle> Queue<T> newBusyWaitParticleQueue(int initSize) {
		return BusyWaitEvictingQueue.newInstance(initSize,
			AsyncParticlesConfig.MAX_PARTICLE_LIMIT);
	}

	public static void onClearParticles() {
		AsyncTickBehavior.getInstance().reset();
		GpuParticleBehavior.getInstance().onClearParticles();
		// compat disabled
		// compat disabled
	}

	public static void doFirstRefresh(Particle particle) {
		LightCachedParticleAddon addon = (LightCachedParticleAddon) particle;
		boolean b = ConfigHelper.particleLightCache() && !addon.asyncparticles$isStaticLight();
		if (b && !(ConfigHelper.isGpuParticles()
			&& particle instanceof SingleQuadParticle tsp
			&& GpuParticleBehavior.getInstance().canRenderFast(tsp))) {
			addon.asyncparticles$enableLightCache(true);
		}
		Integer i = DESTRUCTION_LIGHT_CACHE.get();
		if (i != null) {
			addon.asyncparticles$setLight(i);
		} else if (b) {
			addon.asyncparticles$refresh();
		} else if (ConfigHelper.isGpuParticles()
			&& particle instanceof SingleQuadParticle tsp
			&& GpuParticleBehavior.getInstance().canRenderFast(tsp)) {
			// We just use the light cache slot for gpu, but never really enable it.
			int lightColor;
			try {
				lightColor = addon.asyncparticles$invoke_getLightCoords(0f);
			} catch (MissingPaletteEntryException ignore) {
				lightColor = 0;
			}
			addon.asyncparticles$setLight(lightColor);
		}
	}

}