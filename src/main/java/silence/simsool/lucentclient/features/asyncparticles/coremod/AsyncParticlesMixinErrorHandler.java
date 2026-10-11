package silence.simsool.lucentclient.features.asyncparticles.coremod;

import silence.simsool.lucentclient.features.asyncparticles.coremod.adjusters.AdjusterParticlesAsyncTickableGroup;
import silence.simsool.lucentclient.features.asyncparticles.coremod.mixin_extension.class_adjuster.MixinClassAdjusterApplication;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinErrorHandler;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class AsyncParticlesMixinErrorHandler implements IMixinErrorHandler {
	@Override
	public ErrorAction onPrepareError(IMixinConfig config, Throwable th, IMixinInfo mixin, ErrorAction action) {
		return null;
	}

	@Override
	public ErrorAction onApplyError(String targetClassName, Throwable th, IMixinInfo mixin, ErrorAction action) {
		String className = mixin.getClassName();
		MixinClassAdjusterApplication app = MixinClassAdjusterApplication.getInstance();
		if (app != null) {
			String originalMixin = app.getOriginalMixin(className);
			if (AdjusterParticlesAsyncTickableGroup.CLASS_NAME.equals(originalMixin)) {
				return ErrorAction.WARN;
			}
		}
		return null;
	}
}