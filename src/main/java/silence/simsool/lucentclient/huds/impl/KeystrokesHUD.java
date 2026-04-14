package silence.simsool.lucentclient.huds.impl;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.gui.GuiGraphics;
import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.general.utils.UDisplay;
import silence.simsool.lucent.general.utils.UText;
import silence.simsool.lucent.ui.utils.UIColors;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.mods.impl.hud.KeystrokesMod;

public class KeystrokesHUD extends LucentHUD {

	public KeystrokesHUD() {
		super("lucentclient_keystrokes", KeystrokesMod.class, 0.927f, 0.009f, 1.0f, HUDAlignment.LEFT);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.MINECRAFT;
	}

	@Override
	public float getPreviewWidth() {
		return 64 * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
	}

	@Override
	public float getPreviewHeight() {
		return 64 * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
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
		
		int sw = UDisplay.getGuiScaledWidth();
		int sh = UDisplay.getGuiScaledHeight();

		float rx = x * sw;
		float ry = y * sh;

		float scaledW = 64 * scale;

		if (alignment == HUDAlignment.CENTER) rx -= (scaledW / 2f);
		else if (alignment == HUDAlignment.RIGHT) rx -= scaledW;

		float gs = scale;
		float bw = 20f * gs;
		float gap = 2f * gs;

		// W A S D
		boolean w = !preview && mc.options.keyUp.isDown();
		boolean a = !preview && mc.options.keyLeft.isDown();
		boolean s = !preview && mc.options.keyDown.isDown();
		boolean d = !preview && mc.options.keyRight.isDown();

		drawKey(guiGraphics, "W", rx + bw + gap, ry, bw, bw, w);
		drawKey(guiGraphics, "A", rx, ry + bw + gap, bw, bw, a);
		drawKey(guiGraphics, "S", rx + bw + gap, ry + bw + gap, bw, bw, s);
		drawKey(guiGraphics, "D", rx + (bw + gap) * 2, ry + bw + gap, bw, bw, d);
		
		float mbw = (bw * 3 + gap * 2) / 2f - gap / 2f;
		boolean lmb = !preview && mc.options.keyAttack.isDown();
		boolean rmb = !preview && mc.options.keyUse.isDown();
		
		drawKey(guiGraphics, "LMB", rx, ry + (bw + gap) * 2, mbw, bw, lmb);
		drawKey(guiGraphics, "RMB", rx + mbw + gap, ry + (bw + gap) * 2, mbw, bw, rmb);
	}

	private void drawKey(GuiGraphics guiGraphics, String key, float x, float y, float w, float h, boolean pressed) {
		int bg = pressed ? UIColors.withAlpha(UIColors.PURE_WHITE, 120) : UIColors.withAlpha(UIColors.PURE_BLACK, 100);
		int fg = pressed ? UIColors.PURE_BLACK : UIColors.PURE_WHITE;

		guiGraphics.fill((int)x, (int)y, (int)(x + w), (int)(y + h), bg);
		UText.drawCenteredText(guiGraphics, key, x + w / 2f, y + (h - 9f * scale) / 2f, scale, fg);
	}

}