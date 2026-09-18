package silence.simsool.lucentclient.mixin.mixins.optimization;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(targets = "net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer$Group")
public abstract class MixinRenderTypeFeatureRenderer_Group {

	@ModifyVariable(method = "<init>", at = @At("HEAD"), name = "canReorder", argsOnly = true)
	private static boolean forceReorderability(final boolean canReorder) {
		if (FastRenderMod.isEnabled() && FastRenderMod.EnhancedBatching) {
			return true;
		}
		return canReorder;
	}

}