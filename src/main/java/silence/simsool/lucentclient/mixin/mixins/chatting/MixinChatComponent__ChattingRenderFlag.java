package silence.simsool.lucentclient.mixin.mixins.chatting;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import silence.simsool.lucentclient.mods.impl.utility.ChattingMod;
import silence.simsool.lucentclient.mods.impl.utility.chatting.ChatScrolling;
import silence.simsool.lucentclient.mods.impl.utility.chatting.SmoothChat;

@Mixin(ChatComponent.class)
public class MixinChatComponent__ChattingRenderFlag {

	@Shadow
	private int chatScrollbarPos;

	@Unique
	private boolean chatting$posed = false;

	@ModifyVariable(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IIIZZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private boolean modifyIsChatting(boolean isChatting) {
		return isChatting || ChattingMod.isPeeking();
	}

	@Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IIIZZ)V", at = @At("HEAD"))
	private void onRenderHead(GuiGraphics graphics, Font font, int ticks, int mouseX, int mouseY, boolean isChatting, boolean changeCursorOnInsertions, CallbackInfo ci) {
		ChattingMod.checkPeeking();
		ChattingMod.isRenderingChat = true;
		ChatScrolling.step(this.chatScrollbarPos);
		float smoothDy = SmoothChat.translateY(this.chatScrollbarPos > 0);
		if (smoothDy != 0.0f) {
			this.chatting$posed = true;
			graphics.pose().pushMatrix();
			graphics.pose().translate(0.0f, smoothDy);
		} else {
			this.chatting$posed = false;
		}
	}

	@Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IIIZZ)V", at = @At("RETURN"))
	private void onRenderReturn(GuiGraphics graphics, Font font, int ticks, int mouseX, int mouseY, boolean isChatting, boolean changeCursorOnInsertions, CallbackInfo ci) {
		if (this.chatting$posed) {
			graphics.pose().popMatrix();
			this.chatting$posed = false;
		}
		ChattingMod.isRenderingChat = false;
	}

	@Inject(method = "addMessageToDisplayQueue", at = @At("HEAD"))
	private void onAddMessageHead(GuiMessage guiMessage, CallbackInfo ci) {
		SmoothChat.start();
	}

	@Inject(method = "scrollChat", at = @At("HEAD"))
	private void onScrollChat(int posDelta, CallbackInfo ci) {
		ChatScrolling.shouldSmooth = true;
	}

	@Redirect(method = {"forEachLine", "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;chatScrollbarPos:I", opcode = Opcodes.GETFIELD))
	private int redirectChatScrollbarPos(ChatComponent instance) {
		if (ChattingMod.isEnabled() && ChattingMod.SmoothScrolling) {
			return ChatScrolling.pos();
		}
		return this.chatScrollbarPos;
	}
}