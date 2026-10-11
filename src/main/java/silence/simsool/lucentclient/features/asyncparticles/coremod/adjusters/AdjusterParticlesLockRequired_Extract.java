package silence.simsool.lucentclient.features.asyncparticles.coremod.adjusters;

import silence.simsool.lucentclient.features.asyncparticles.config.MixinConfigHelper;
import silence.simsool.lucentclient.features.asyncparticles.coremod.mixin_extension.class_adjuster.MixinClassAdjuster;

import java.util.List;

public class AdjusterParticlesLockRequired_Extract implements MixinClassAdjuster {
	private static final String CLASS_NAME = "silence.simsool.lucentclient.mixin.mixins.asyncparticles.conditional.MixinParticles_LockRequired_Extract";
	@Override
	public String getMixinClassName() {
		return CLASS_NAME;
	}

	@Override
	public List<String> getTargets(List<String> originalTargets) {
		return List.copyOf(MixinConfigHelper.getLockRequired());
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}
}