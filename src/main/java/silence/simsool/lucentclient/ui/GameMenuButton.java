package silence.simsool.lucentclient.ui;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class GameMenuButton extends Button {

	public GameMenuButton(int x, int y, int width, int height, Component message, OnPress onPress) {
		super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		boolean hovered = this.isHovered();

		int bgColor = hovered ? 0x902B2F3A : 0x70000000;
		int borderColor = hovered ? 0xFFFFFFFF : 0x30FFFFFF;
		int textColor = hovered ? 0xFFFFFFFF : 0xFFCCCCCC;

		int x = this.getX();
		int y = this.getY();
		int w = this.width;
		int h = this.height;

		// fill
		graphics.fill(x, y, x + w, y + h, bgColor);

		// outline
		graphics.fill(x, y, x + w, y + 1, borderColor);
		graphics.fill(x, y + h - 1, x + w, y + h, borderColor);
		graphics.fill(x, y + 1, x + 1, y + h - 1, borderColor);
		graphics.fill(x + w - 1, y + 1, x + w, y + h - 1, borderColor);

		int textWidth = mc.font.width(this.getMessage());
		graphics.text(mc.font, this.getMessage(), x + (w - textWidth) / 2, y + (h - 8) / 2, textColor);
	}

}