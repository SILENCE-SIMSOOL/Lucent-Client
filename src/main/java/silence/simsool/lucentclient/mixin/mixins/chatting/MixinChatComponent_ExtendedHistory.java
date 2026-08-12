package silence.simsool.lucentclient.mixin.mixins.chatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.gui.components.ChatComponent;
import silence.simsool.lucentclient.LucentClient;

@Mixin(ChatComponent.class)
public class MixinChatComponent_ExtendedHistory {

	@ModifyConstant(method = "*", constant = @Constant(intValue = 100))
	private int extendChatHistoryLimit(int original) {
		return LucentClient.MAX_CHAT_HISTORY;
	}

}