package silence.simsool.lucentclient.features.controlling.api.entries;

import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public interface IKeyEntry {
	Component categoryName();
	KeyMapping getKey();
	Component getName();
	Button getBtnResetKeyBinding();
	Button getBtnChangeKeyBinding();
	List<GuiEventListener> children();
	boolean mouseClicked(MouseButtonEvent event, boolean doubleClick);
	boolean mouseReleased(MouseButtonEvent event);
	void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTicks);
}