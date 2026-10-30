package silence.simsool.lucentclient.features.asyncparticles.coremod.adjusters;

import silence.simsool.lucentclient.features.asyncparticles.config.MixinConfigHelper;
import silence.simsool.lucentclient.features.asyncparticles.coremod.MixinUtil;
import silence.simsool.lucentclient.features.asyncparticles.coremod.mixin_extension.class_adjuster.MixinClassAdjuster;

import java.util.List;

public class AdjusterParticlesLockProvider implements MixinClassAdjuster {

	private static final String CLASS_NAME = "silence.simsool.lucentclient.mixin.mixins.asyncparticles.conditional.MixinParticles_LockProvider";
	@Override
	public String getMixinClassName() {
		return CLASS_NAME;
	}

	@Override
	public List<String> getTargets(List<String> originalTargets) {
		return List.copyOf(MixinConfigHelper.getLockProvider());
	}

	@Override
	public String getRefMapperConfig() {
		return MixinUtil.getRefMapperName(CLASS_NAME,"asyncparticles-common-refmap.json");
	}

}