package silence.simsool.lucentclient.mixin.mixins.optimization;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(value = RenderTypes.class, priority = 500)
public abstract class MixinRenderTypes {

	@Redirect(method = {
		"lambda$static$22",
		"lambda$static$23",
		"lambda$static$25"
	}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderSetup$RenderSetupBuilder;sortOnUpload()Lnet/minecraft/client/renderer/rendertype/RenderSetup$RenderSetupBuilder;"))
	private static RenderSetup.RenderSetupBuilder disableTranslucencySorting(final RenderSetup.RenderSetupBuilder instance) {
		if (FastRenderMod.isEnabled() && FastRenderMod.SkipTextSorting) {
			return instance;
		}
		return instance.sortOnUpload();
	}

}