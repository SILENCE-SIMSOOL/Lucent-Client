package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import silence.simsool.lucentclient.mods.impl.hud.PotionEffectsMod;
import silence.simsool.lucentclient.mods.impl.hud.VanillaHUDMod;

@Mixin(Gui.class)
public class MixinGuiPotionEffects {

	@Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
	private void onRenderEffects(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		if (PotionEffectsMod.isEnabled() || (VanillaHUDMod.isEnabled() && !VanillaHUDMod.PotionEffects)) {
			ci.cancel(); // Hide vanilla potion effects
		}
	}

}