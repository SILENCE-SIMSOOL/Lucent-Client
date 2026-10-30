package silence.simsool.lucentclient.features.asyncparticles.coremod.adjusters;

import silence.simsool.lucentclient.features.asyncparticles.config.MixinConfigHelper;
import silence.simsool.lucentclient.features.asyncparticles.coremod.mixin_extension.class_adjuster.MixinClassAdjuster;

import java.util.ArrayList;
import java.util.List;

public class AdjusterParticlesAsyncTickableGroup implements MixinClassAdjuster {

	public static final String CLASS_NAME = "silence.simsool.lucentclient.mixin.mixins.asyncparticles.conditional.MixinAsyncTick_AsyncTickableParticleGroup";
	@Override
	public String getMixinClassName() {
		return CLASS_NAME;
	}

	@Override
	public List<String> getTargets(List<String> originalTargets) {
		ArrayList<String> list = new ArrayList<>(originalTargets);
		list.addAll(MixinConfigHelper.getAsyncTickableParticleGroups());
		return list;
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

}