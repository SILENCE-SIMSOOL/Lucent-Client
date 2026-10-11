package silence.simsool.lucentclient.features.asyncparticles.config;

import silence.simsool.lucentclient.features.asyncparticles.compat.ModListHelper;

import java.util.Collection;

import static silence.simsool.lucentclient.features.asyncparticles.config.AsyncParticlesMixinConfig.CONFIG;

public class MixinConfigHelper {
	public static Collection<String> getNoLightCache() {
		return CONFIG.getNoLightCache().stream().filter(ModListHelper::classExists).toList();
	}

	public static Collection<String> getLockProvider() {
		return CONFIG.getLockProvider().stream().filter(ModListHelper::classExists).toList();
	}

	public static Collection<String> getLockRequired() {
		return CONFIG.getLockRequired().stream().filter(ModListHelper::classExists).toList();
	}

	public static Collection<String> getReplaceRandom() {
		return CONFIG.getReplaceRandom().stream().filter(ModListHelper::classExists).toList();
	}

	public static boolean isSafeLegacyRandomSource() {
		return CONFIG.isSafeLegacyRandomSource();
	}

	public static boolean isSafeClassInstanceMultiMap() {
		return CONFIG.isSafeClassInstanceMultiMap();
	}

	public static boolean isSafeBlockEntityMap() {
		return CONFIG.isSafeBlockEntityMap();
	}

	public static Collection<String> getAsyncTickableParticleGroups() {
		return CONFIG.getAsyncTickableParticleGroups().stream().filter(ModListHelper::classExists).toList();
	}
}