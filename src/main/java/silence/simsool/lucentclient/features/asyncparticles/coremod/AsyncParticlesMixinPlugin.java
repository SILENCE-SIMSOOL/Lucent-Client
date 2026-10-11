package silence.simsool.lucentclient.features.asyncparticles.coremod;

import com.bawnorton.mixinsquared.canceller.MixinCancellerRegistrar;
import com.bawnorton.mixinsquared.ext.ExtensionRegistrar;
import silence.simsool.lucentclient.features.asyncparticles.config.MixinConfigHelper;
import silence.simsool.lucentclient.features.asyncparticles.coremod.adjusters.*;
import silence.simsool.lucentclient.features.asyncparticles.coremod.cancellers.AsyncParticlesMixinCanceller;
import silence.simsool.lucentclient.features.asyncparticles.coremod.cancellers.AsyncParticlesMixinMemberCanceller;
import silence.simsool.lucentclient.features.asyncparticles.coremod.mixin_extension.class_adjuster.MixinClassAdjusterRegistrar;
import silence.simsool.lucentclient.features.asyncparticles.coremod.mixin_extension.member_canceller.ExtensionMemberCancelApplication;
import silence.simsool.lucentclient.features.asyncparticles.coremod.mixin_extension.member_canceller.MixinMemberCancellerRegistrar;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.util.List;
import java.util.Set;

import static silence.simsool.lucentclient.features.asyncparticles.compat.ModListHelper.*;

public class AsyncParticlesMixinPlugin implements IMixinConfigPlugin {
	public static final ILogger LOGGER = MixinService.getService().getLogger("asyncparticles:plugin");

	@Override
	public void onLoad(String mixinPackage) {
		if (!IS_CLIENT) {
			return;
		}
		Mixins.registerErrorHandlerClass(AsyncParticlesMixinErrorHandler.class.getName());

		ExtensionRegistrar.register(new ExtensionMemberCancelApplication());

		MixinClassAdjusterRegistrar.register(new AdjusterParticlesNoLightCache());
		MixinClassAdjusterRegistrar.register(new AdjusterParticlesLockProvider());
		MixinClassAdjusterRegistrar.register(new AdjusterParticlesLockRequired_Tick());
		MixinClassAdjusterRegistrar.register(new AdjusterParticlesLockRequired_Extract());
		MixinClassAdjusterRegistrar.register(new AdjusterReplaceRandom());
		MixinClassAdjusterRegistrar.register(new AdjusterParticlesAsyncTickableGroup());
		MixinMemberCancellerRegistrar.register(new AsyncParticlesMixinMemberCanceller());
		MixinCancellerRegistrar.register(new AsyncParticlesMixinCanceller());

		// compat disabled
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	//	private static final int L = "silence.simsool.lucentclient.mixin.mixins.asyncparticles.".length();
	private static final int PACKAGE_LENGTH = "silence.simsool.lucentclient.mixin.mixins.asyncparticles.".length();

	/// - mixins located in `mixin/fabric` or `mixin/<mod_id>/fabric` package only take effect on fabric.
	/// - mixins located in `mixin/fabric/<mod_id>` take effect on fabric or Sinytra Connector.
	/// - others take effect on any platform.
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (!IS_CLIENT) {
			return false;
		}
		String mixinPackageName = mixinClassName.substring(PACKAGE_LENGTH);
		String[] split = mixinPackageName.split("\\.");
		if (split.length == 1) {
			return true;
		}
		return switch (split[0]) {
			case "conditional" -> switch (split[1]) {
				case "MixinClassInstanceMultiMap_SafeClassInstanceMultiMap_On" -> MixinConfigHelper.isSafeClassInstanceMultiMap();
				case "MixinClassInstanceMultiMap_SafeClassInstanceMultiMap_Off" -> !MixinConfigHelper.isSafeClassInstanceMultiMap();
				case "MixinLevelChunk_SafeBlockEntityMap_On" -> MixinConfigHelper.isSafeBlockEntityMap();
				case "MixinLevelChunk_SafeBlockEntityMap_Off" -> !MixinConfigHelper.isSafeBlockEntityMap();
				case "MixinLegacyRandomSource" -> !ASYNC_LOADED;
				default -> true;
			};
			case "fabric" -> switch (split[1]) {
				case "core" -> !IS_FORGE;
				case "compat" -> throw new IllegalStateException("Unknown fabric compat mixin: " + mixinClassName);
				default -> throw new IllegalArgumentException("Unknown fabric mixin: " + mixinClassName);
			};
			case "core", "off_thread_access" -> true;
			case "compat" -> false;
			default -> throw new IllegalArgumentException("Unknown mixin: " + mixinClassName);
		};
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

}