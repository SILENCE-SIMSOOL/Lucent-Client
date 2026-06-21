package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.network.chat.Component;
import silence.simsool.lucentclient.mods.impl.hud.VanillaHUDMod;

@Mixin(BossHealthOverlay.class)
public abstract class MixinBossHealthOverlay {

	@Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true, remap = false)
	private void onRender(GuiGraphicsExtractor guiGraphics, CallbackInfo ci) {
		if (!VanillaHUDMod.BossBar) {
			ci.cancel();
		}
	}

	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"), remap = false)
	private void redirectBossBarText(GuiGraphicsExtractor graphics, Font font, Component text, int x, int y, int color) {
		graphics.text(font, text, x, y, color, VanillaHUDMod.BossBarShadow);
	}

}