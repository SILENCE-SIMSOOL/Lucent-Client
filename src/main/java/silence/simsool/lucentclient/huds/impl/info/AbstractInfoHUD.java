package silence.simsool.lucentclient.huds.impl.info;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.gui.GuiGraphics;
import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.utils.UDisplay;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;

public abstract class AbstractInfoHUD extends LucentHUD {

	protected AbstractInfoHUD(String id, Class<? extends Mod> moduleClass, float defaultX, float defaultY, HUDAlignment defaultAlignment) {
		super(id, moduleClass, defaultX, defaultY, 1.0f, defaultAlignment);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.MINECRAFT;
	}

	protected abstract String getLabel();
	protected abstract String getValue(boolean preview);
	protected abstract boolean isReverseOrder();
	protected abstract boolean isShowBrackets();
	protected abstract boolean isShowShadow();
	protected abstract int getTextColor();
	protected abstract boolean isShowBackground();
	protected abstract int getBackgroundColor();

	protected String getFormattedText(boolean preview) {
		String label = getLabel();
		String val = getValue(preview);
		String result;
		if (isReverseOrder()) {
			result = val + " " + label;
		} else {
			result = label + ": " + val;
		}
		if (isShowBrackets()) {
			result = "[" + result + "]";
		}
		return result;
	}

	@Override
	public float getPreviewWidth() {
		String text = getFormattedText(true);
		float w = mc.font.width(text);
		if (isShowBackground()) w += 8;
		return (w + 4) * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
	}

	@Override
	public float getPreviewHeight() {
		float h = 9;
		if (isShowBackground()) h = 18;
		return (h + 4) * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
	}

	@Override
	public void draw(GuiGraphics guiGraphics) {
		if (isEditHudOpen || UDisplay.isDebugScreen()) return;
		render(guiGraphics, false);
	}

	@Override
	public void preview(GuiGraphics guiGraphics) {
		render(guiGraphics, true);
	}

	private void render(GuiGraphics guiGraphics, boolean preview) {
		if (!preview && mc.player == null) return;

		String text = getFormattedText(preview);
		int sw = UDisplay.getGuiScaledWidth();
		int sh = UDisplay.getGuiScaledHeight();

		float rx = x * sw;
		float ry = y * sh;

		// Scaled width for alignment
		float scaledW = getScaledWidth() / ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());

		if (alignment == HUDAlignment.CENTER) rx -= (getScaledWidth() / 2f);
		else if (alignment == HUDAlignment.RIGHT) rx -= getScaledWidth();

		float tw = mc.font.width(text) * scale;
		float th = 9 * scale;

		if (isShowBackground()) {
			float bw = tw + 8 * scale;
			float bh = 18 * scale;
			float bx = rx - 4 * scale;
			float by = ry - 4.5f * scale;
			guiGraphics.fill((int)bx, (int)by, (int)(bx + bw), (int)(by + bh), getBackgroundColor());
		}

		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().translate(rx, ry);
		guiGraphics.pose().scale(scale, scale);
		guiGraphics.drawString(mc.font, text, 0, 0, getTextColor(), isShowShadow());
		guiGraphics.pose().popMatrix();
	}
}
