package silence.simsool.lucentclient.mixin.mixins.chatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import silence.simsool.lucent.general.utils.UMouse;
import silence.simsool.lucentclient.mods.impl.utility.ChattingMod;

@Mixin(ChatScreen.class)
public class MixinChatScreen_Chatting {

	@Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
	private void onMouseClicked(MouseButtonEvent event, boolean isDoubleClick, CallbackInfoReturnable<Boolean> cir) {
		if (ChattingMod.isEnabled() && ChattingMod.ChatCopy) {
			if (ChattingMod.CopyKey.isMouse() && event.button() == ChattingMod.CopyKey.mouseButton) {
				if (ChattingMod.copyAt(event.x(), event.y())) {
					cir.setReturnValue(true);
				}
			}
		}
	}

	@Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
	private void onKeyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
		if (ChattingMod.isEnabled() && ChattingMod.ChatCopy) {
			if (!ChattingMod.CopyKey.isMouse() && event.key() == ChattingMod.CopyKey.keyCode) {
				if (ChattingMod.copyAt(UMouse.getScaledX(), UMouse.getScaledY())) {
					cir.setReturnValue(true);
				}
			}
		}
	}

}