package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import silence.simsool.lucentclient.mods.impl.hud.VanillaHUDMod;

@Mixin(Hud.class)
public class MixinGui {

	@Inject(method = "extractArmor", at = @At("HEAD"), cancellable = true, remap = false)
	private static void onRenderArmor(GuiGraphicsExtractor guiGraphics, Player player, int i, int j, int k, int l, CallbackInfo ci) {
		if (!VanillaHUDMod.ArmorBar) ci.cancel();
	}

	@Inject(method = "extractHearts", at = @At("HEAD"), cancellable = true, remap = false)
	private void onRenderHearts(GuiGraphicsExtractor guiGraphics, Player player, int i, int j, int k, int l, float f, int m, int n, int o, boolean bl, CallbackInfo ci) {
		if (!VanillaHUDMod.HealthBar) {
			ci.cancel();
		}
	}

	@Inject(method = "extractFood", at = @At("HEAD"), cancellable = true, remap = false)
	private void onRenderFood(GuiGraphicsExtractor guiGraphics, Player player, int i, int j, CallbackInfo ci) {
		if (!VanillaHUDMod.HungerBar) {
			ci.cancel();
		}
	}

	@ModifyExpressionValue(method = "extractHotbarAndDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;hasExperience()Z"), remap = false)
	private boolean onCancelXPLevel(boolean original) {
		//if (DevConfig.HIDE_XP_BAR) return false;
		return original;
	}

}