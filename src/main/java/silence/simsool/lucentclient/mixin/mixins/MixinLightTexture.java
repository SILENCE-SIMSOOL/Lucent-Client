package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.renderer.LightTexture;
import silence.simsool.lucentclient.mods.impl.graphics.FullbrightMod;

@Mixin(LightTexture.class)
public class MixinLightTexture {

	@ModifyVariable(method = "updateLightTexture", at = @At(value = "STORE"), ordinal = 6)
	private float modifyBrightness(float originalBrightness) {
		if (FullbrightMod.isEnabled()) return FullbrightMod.BrightnessLevel;
		return originalBrightness;
	}

}