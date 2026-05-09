package silence.simsool.lucentclient.ducks;

import net.minecraft.client.GuiMessage;

public interface IChatComponent {

	GuiMessage getMessageAt(double mouseX, double mouseY);

}