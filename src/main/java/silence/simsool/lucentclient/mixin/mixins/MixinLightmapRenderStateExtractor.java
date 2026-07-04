package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import silence.simsool.lucentclient.mods.impl.graphics.FullbrightMod;

@Mixin(LightmapRenderStateExtractor.class)
abstract class MixinLightmapRenderStateExtractor {

	@ModifyExpressionValue(method = "extract", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F", ordinal = 0))
	private float allowNegativeGamma(float original) {
		float gamma = FullbrightMod.BrightnessLevel;
		if (gamma < 0) return gamma;
		return original;
	}

}