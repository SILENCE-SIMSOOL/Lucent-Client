package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import silence.simsool.lucentclient.mods.impl.hud.PerformanceMod;
import silence.simsool.lucentclient.mods.impl.hud.PingMod;
import silence.simsool.lucentclient.mods.impl.utility.BetterScreensMod;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;showNetworkCharts()Z"))
	private boolean alwaysSendPing(boolean original) {
		if (PingMod.isEnabled() || PerformanceMod.isEnabled()) return true;
		return original;
	}

	@Inject(method = "handleContainerClose", at = @At("HEAD"), cancellable = true)
	private void onHandleContainerClose(ClientboundContainerClosePacket packet, CallbackInfo ci) {
		if (BetterScreensMod.isEnabled() && BetterScreensMod.PreventClosingScreens) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.screen != null && (mc.screen instanceof PauseScreen || mc.screen instanceof OptionsScreen || mc.screen instanceof ChatScreen)) {
				ci.cancel();
			}
		}
	}

	@Inject(method = "handleOpenScreen", at = @At("HEAD"), cancellable = true)
	private void onHandleOpenScreen(ClientboundOpenScreenPacket packet, CallbackInfo ci) {
		if (BetterScreensMod.isEnabled() && BetterScreensMod.PreventClosingScreens) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.screen != null && (mc.screen instanceof PauseScreen || mc.screen instanceof OptionsScreen || mc.screen instanceof ChatScreen)) {
				ci.cancel();
			}
		}
	}
}