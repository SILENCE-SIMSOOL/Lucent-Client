package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import silence.simsool.lucentclient.features.fastrender.IIris_OuterWrappedRenderType;
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

	@ModifyExpressionValue(method = "getOrAddDraw", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/feature/RenderTypeFeatureRenderer$Group;canReorder:Z", opcode = Opcodes.GETFIELD))
	private boolean fixOrder(final boolean original, @Local(name = "renderType", argsOnly = true) final RenderType renderType) {
		if (!FastRenderMod.isEnabled() || !FastRenderMod.EnhancedBatching) {
			return original;
		}
		if (immediatelyFast$isBannerPattern(renderType)) {
			return false;
		} else if (renderType instanceof IIris_OuterWrappedRenderType wrappedRenderType && immediatelyFast$isBannerPattern(wrappedRenderType.immediatelyFast$getWrapped())) {
			return false;
		} else {
			return original;
		}
	}

	@Unique
	private static boolean immediatelyFast$isBannerPattern(final RenderType renderType) {
		return renderType.state.textures.get("Sampler0") instanceof RenderSetup.TextureBinding textureBinding && textureBinding.location().equals(Sheets.BANNER_SHEET);
	}

}