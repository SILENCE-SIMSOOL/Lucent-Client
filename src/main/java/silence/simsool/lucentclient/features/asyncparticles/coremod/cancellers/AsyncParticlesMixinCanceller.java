package silence.simsool.lucentclient.features.asyncparticles.coremod.cancellers;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import silence.simsool.lucentclient.features.asyncparticles.config.MixinConfigHelper;

import java.util.List;

public class AsyncParticlesMixinCanceller implements MixinCanceller {

	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
		if (mixinClassName.startsWith("net.caffeinemc.mods.lithium.mixin.collections.entity_by_type")) {
			return MixinConfigHelper.isSafeClassInstanceMultiMap();
		}
		if (mixinClassName.equals("org.embeddedt.modernfix.common.mixin.devenv.MinecraftMixin")) {
			return true;
		}
		return false;
	}

}