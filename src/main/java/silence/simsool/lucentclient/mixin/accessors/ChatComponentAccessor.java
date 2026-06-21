package silence.simsool.lucentclient.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.gui.components.ChatComponent;

@Mixin(ChatComponent.class)
public interface ChatComponentAccessor {

	@Accessor("allMessages")
	List<GuiMessage> getAllMessages();

	@Accessor("trimmedMessages")
	List<?> getTrimmedMessages();

	@Invoker("getScale")
	double invokeGetScale();

	@Invoker("getLinesPerPage")
	int invokeGetLinesPerPage();

	@Accessor("chatScrollbarPos")
	int getChatScrollbarPos();

}