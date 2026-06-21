package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.Lightmap;
import net.minecraft.world.level.dimension.DimensionType;
import silence.simsool.lucentclient.mods.impl.graphics.FullbrightMod;

@Mixin(Lightmap.class)
public class MixinLightTexture {

	@Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
	private static void onGetBrightness(DimensionType dimensionType, int lightLevel, CallbackInfoReturnable<Float> cir) {
		if (FullbrightMod.isEnabled()) {
			cir.setReturnValue(FullbrightMod.BrightnessLevel);
		}
	}

}