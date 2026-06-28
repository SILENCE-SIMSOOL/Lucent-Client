package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.CameraType;
import silence.simsool.lucentclient.mods.impl.hud.VanillaHUDMod;

@Mixin(Gui.class)
public class MixinGui {

	@Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
	private static void onRenderArmor(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, CallbackInfo ci) {
		if (!VanillaHUDMod.ArmorBar) ci.cancel();
	}

	@Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
	private void onRenderHearts(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, float f, int m, int n, int o, boolean bl, CallbackInfo ci) {
		if (!VanillaHUDMod.HealthBar) {
			ci.cancel();
		}
	}

	@Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
	private void onRenderFood(GuiGraphics guiGraphics, Player player, int i, int j, CallbackInfo ci) {
		if (!VanillaHUDMod.HungerBar) {
			ci.cancel();
		}
	}

	@ModifyExpressionValue(method = "renderHotbarAndDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;hasExperience()Z"))
	private boolean onCancelXPLevel(boolean original) {
		//if (DevConfig.HIDE_XP_BAR) return false;
		return original;
	}

	@ModifyExpressionValue(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
	private boolean onIsFirstPerson(boolean original) {
		if (VanillaHUDMod.isEnabled() && VanillaHUDMod.ThirdPersonCrosshair) {
			return true;
		}
		return original;
	}

}