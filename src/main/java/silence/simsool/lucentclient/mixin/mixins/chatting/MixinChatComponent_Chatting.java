package silence.simsool.lucentclient.mixin.mixins.chatting;

import static silence.simsool.lucent.Lucent.mc;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.ChatComponent;
import silence.simsool.lucent.general.utils.useful.UDisplay;
import silence.simsool.lucentclient.ducks.IChatComponent;
import silence.simsool.lucentclient.mixin.accessors.ChatComponentAccessor;

@Mixin(ChatComponent.class)
public abstract class MixinChatComponent_Chatting implements IChatComponent {

	@Override
	public GuiMessage getMessageAt(double mouseX, double mouseY) {
		ChatComponentAccessor accessor = (ChatComponentAccessor) this;
		if (accessor.getTrimmedMessages().isEmpty()) return null;

		Options options = mc.options;
		double scale = accessor.invokeGetScale();
		int screenHeight = UDisplay.getGuiScaledHeight();
		int chatBottom = (int) Math.floor((screenHeight - 40) / scale);
		int messageHeight = 9;
		double chatLineSpacing = options.chatLineSpacing().get();
		int entryHeight = (int)(messageHeight * (chatLineSpacing + 1.0));

		double localY = mouseY / scale;

		int perPage = accessor.invokeGetLinesPerPage();
		int scrollPos = accessor.getChatScrollbarPos();
		int totalTrimmed = accessor.getTrimmedMessages().size();

		for (int i = 0; i < Math.min(totalTrimmed - scrollPos, perPage); i++) {
			int entryBottom = chatBottom - i * entryHeight;
			int entryTop = entryBottom - entryHeight;

			if (localY >= entryTop && localY <= entryBottom) {
				int trimmedIndex = i + scrollPos;
				int count = 0;
				for (GuiMessage msg : accessor.getAllMessages()) {
					int msgLines = msg.splitLines(mc.font, (int) Math.floor(ChatComponent.getWidth(options.chatWidth().get()) / scale)).size();
					if (trimmedIndex >= count && trimmedIndex < count + msgLines) return msg;
					count += msgLines;
				}
				break;
			}
		}
		return null;
	}

}