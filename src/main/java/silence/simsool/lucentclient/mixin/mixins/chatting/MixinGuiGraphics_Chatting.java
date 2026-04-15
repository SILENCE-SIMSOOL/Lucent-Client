package silence.simsool.lucentclient.mixin.mixins.chatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.gui.GuiGraphics;
import silence.simsool.lucentclient.mods.impl.utility.ChattingMod;

@Mixin(GuiGraphics.class)
public class MixinGuiGraphics_Chatting {

	@ModifyVariable(method = "fill(IIIII)V", at = @At("HEAD"), argsOnly = true, index = 5)
	private int changeFillColor(int color) {
		if (ChattingMod.isRenderingChat && ChattingMod.isEnabled()) {
			if (ChattingMod.ChatBackground) return ChattingMod.BackgroundColor.getRGB();
			else return 0; // Transparent
		}
		return color;
	}

	@ModifyVariable(method = "drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)V", at = @At("HEAD"), argsOnly = true, index = 6)
	private boolean changeTextShadow(boolean shadow) {
		if (ChattingMod.isRenderingChat && ChattingMod.isEnabled()) {
			return ChattingMod.ChatTextShadow;
		}
		return shadow;
	}

	@ModifyVariable(method = "drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V", at = @At("HEAD"), argsOnly = true, index = 6)
	private boolean changeTextShadowString(boolean shadow) {
		if (ChattingMod.isRenderingChat && ChattingMod.isEnabled()) {
			return ChattingMod.ChatTextShadow;
		}
		return shadow;
	}

	@ModifyVariable(method = "drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)V", at = @At("HEAD"), argsOnly = true, index = 6)
	private boolean changeTextShadowComponent(boolean shadow) {
		if (ChattingMod.isRenderingChat && ChattingMod.isEnabled()) {
			return ChattingMod.ChatTextShadow;
		}
		return shadow;
	}

}