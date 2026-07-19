package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import silence.simsool.lucentclient.mods.impl.hud.PotionEffectsMod;

@Mixin(EffectsInInventory.class)
public class MixinEffectsInInventory {

	@ModifyReturnValue(method = "canSeeEffects", at = @At("RETURN"))
	private boolean onCanSeeEffects(boolean original) {
		if (PotionEffectsMod.isEnabled() && !PotionEffectsMod.ShowInventoryEffects) {
			return false;
		}
		return original;
	}

	@Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
	private void onExtractRenderState(GuiGraphicsExtractor guiGraphicsExtractor, int i, int j, CallbackInfo ci) {
		if (PotionEffectsMod.isEnabled() && !PotionEffectsMod.ShowInventoryEffects) {
			ci.cancel();
		}
	}

}