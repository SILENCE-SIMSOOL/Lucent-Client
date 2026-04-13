package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.network.chat.Component;
import silence.simsool.lucentclient.mods.impl.hud.VanillaHUDMod;

@Mixin(BossHealthOverlay.class)
public abstract class MixinBossHealthOverlay {

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onRender(GuiGraphics guiGraphics, CallbackInfo ci) {
		if (!VanillaHUDMod.BossBar) {
			ci.cancel();
		}
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"))
	private void redirectBossBarText(GuiGraphics graphics, Font font, Component text, int x, int y, int color) {
		graphics.drawString(font, text, x, y, color, VanillaHUDMod.BossBarShadow);
	}

}