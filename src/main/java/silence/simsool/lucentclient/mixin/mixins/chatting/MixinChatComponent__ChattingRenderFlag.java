package silence.simsool.lucentclient.mixin.mixins.chatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ChatComponent;
import silence.simsool.lucentclient.mods.impl.utility.ChattingMod;

@Mixin(ChatComponent.class)
public class MixinChatComponent__ChattingRenderFlag {

	@Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IIIZZ)V", at = @At("HEAD"))
	private void onRenderHead(GuiGraphics graphics, Font font, int ticks, int mouseX, int mouseY, boolean isChatting, boolean changeCursorOnInsertions, CallbackInfo ci) {
		ChattingMod.isRenderingChat = true;
	}

	@Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IIIZZ)V", at = @At("RETURN"))
	private void onRenderReturn(GuiGraphics graphics, Font font, int ticks, int mouseX, int mouseY, boolean isChatting, boolean changeCursorOnInsertions, CallbackInfo ci) {
		ChattingMod.isRenderingChat = false;
	}

}