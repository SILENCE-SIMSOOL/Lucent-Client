package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.state.LightmapRenderState;
import silence.simsool.lucentclient.mods.impl.graphics.FullbrightMod;

@Mixin(Lightmap.class)
public class MixinLightMap {

	@Inject(method = "render", at = @At("HEAD"))
	private void modifyBrightness(LightmapRenderState renderState, CallbackInfo ci) {
		if (FullbrightMod.isEnabled()) {
			renderState.brightness = FullbrightMod.BrightnessLevel;
		}
	}

}