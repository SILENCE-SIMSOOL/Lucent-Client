package silence.simsool.lucentclient.mixin.mixins;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import silence.simsool.lucentclient.mods.impl.hud.PerformanceMod;
import silence.simsool.lucentclient.mods.impl.hud.PingMod;
import silence.simsool.lucentclient.mods.impl.utility.BetterScreensMod;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

	@Unique
	private static final Set<Class<?>> PREVENT_SERVER_FROM_CLOSING = Set.of(
		PauseScreen.class,
		OptionsScreen.class,
		PackSelectionScreen.class,
		SoundOptionsScreen.class,
		VideoSettingsScreen.class,
		ControlsScreen.class,
		LanguageSelectScreen.class,
		AccessibilityOptionsScreen.class,
		SocialInteractionsScreen.class
	);

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;showNetworkCharts()Z"))
	private boolean alwaysSendPing(boolean original) {
		if (PingMod.isEnabled() || PerformanceMod.isEnabled()) return true;
		return original;
	}

	@Inject(method = "handleContainerClose", at = @At("HEAD"), cancellable = true)
	private void handleChatClose(ClientboundContainerClosePacket packet, CallbackInfo ci) {
		if (BetterScreensMod.isEnabled() && BetterScreensMod.PreventClosingScreens) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.screen instanceof ChatScreen) {
				ci.cancel();
			}
		}
	}

	@Inject(method = "handleContainerClose", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V", shift = At.Shift.AFTER), cancellable = true)
	private void handleContainerClose(ClientboundContainerClosePacket packet, CallbackInfo ci) {
		if (BetterScreensMod.isEnabled() && BetterScreensMod.PreventClosingScreens) {
			Minecraft mc = Minecraft.getInstance();
			Screen currentScreen = mc.screen;
			if (currentScreen != null && PREVENT_SERVER_FROM_CLOSING.contains(currentScreen.getClass())) {
				ci.cancel();
			}
		}
	}

	@Inject(method = "handleOpenScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V", shift = At.Shift.AFTER))
	private void handleOpenScreenHead(ClientboundOpenScreenPacket packet, CallbackInfo ci, @Share("oldScreenRef") LocalRef<Screen> oldScreenRef) {
		if (BetterScreensMod.isEnabled()) {
			Minecraft mc = Minecraft.getInstance();
			oldScreenRef.set(mc.screen);
		}
	}

	@Inject(method = "handleOpenScreen", at = @At("RETURN"))
	private void handleOpenScreenReturn(ClientboundOpenScreenPacket packet, CallbackInfo ci, @Share("oldScreenRef") LocalRef<Screen> oldScreenRef) {
		if (BetterScreensMod.isEnabled() && BetterScreensMod.PreventClosingScreens) {
			Screen oldScreen = oldScreenRef.get();
			Minecraft mc = Minecraft.getInstance();
			Screen currentScreen = mc.screen;
			if (oldScreen == currentScreen) {
				return;
			}
			if (oldScreen != null && PREVENT_SERVER_FROM_CLOSING.contains(oldScreen.getClass())) {
				if (currentScreen instanceof AbstractContainerScreen<?> containerScreen && containerScreen.shouldCloseOnEsc()) {
					containerScreen.onClose();
					currentScreen = mc.screen;
					if (currentScreen == null) {
						mc.setScreen(oldScreen);
					}
				}
			}
		}
	}
}