package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import silence.simsool.lucentclient.mods.impl.hud.VanillaHUDMod;

@Mixin(BossHealthOverlay.class)
public abstract class MixinBossHealthOverlay {

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onRender(GuiGraphics guiGraphics, CallbackInfo ci) {
		if (!VanillaHUDMod.BossBar) ci.cancel();
	}

	@ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I"), index = 5)
	private boolean modifyBossBarShadow(boolean originalShadow) {
		return VanillaHUDMod.BossBarShadow;
	}

}