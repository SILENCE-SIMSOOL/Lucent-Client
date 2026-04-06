	package silence.simsool.lucentclient.huds.impl;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.gui.GuiGraphics;
import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.ui.utils.UIColors;
import silence.simsool.lucent.ui.utils.nvg.Fonts;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.mods.impl.hud.KeystrokesMod;

public class KeystrokesHUD extends LucentHUD {

	public KeystrokesHUD() {
		super("keystrokes", KeystrokesMod.class, 0.05f, 0.4f, 1.0f, HUDAlignment.LEFT);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.NANOVG;
	}

	@Override
	public void draw(GuiGraphics guiGraphics) {
		if (mc.player == null) return;
		render(guiGraphics, false);
	}

	@Override
	public void preview(GuiGraphics guiGraphics) {
		render(guiGraphics, true);
	}

	private void render(GuiGraphics guiGraphics, boolean preview) {
		float rx = getRenderX();
		float ry = getRenderY();
		float gs = scale;
		float bw = 20f * gs;
		float gap = 2f * gs;

		// W A S D
		boolean w = !preview && mc.options.keyUp.isDown();
		boolean a = !preview && mc.options.keyLeft.isDown();
		boolean s = !preview && mc.options.keyDown.isDown();
		boolean d = !preview && mc.options.keyRight.isDown();

		drawKey("W", rx + bw + gap, ry, bw, bw, w);
		drawKey("A", rx, ry + bw + gap, bw, bw, a);
		drawKey("S", rx + bw + gap, ry + bw + gap, bw, bw, s);
		drawKey("D", rx + (bw + gap) * 2, ry + bw + gap, bw, bw, d);
		
		float mbw = (bw * 3 + gap * 2) / 2f - gap / 2f;
		boolean lmb = !preview && mc.options.keyAttack.isDown();
		boolean rmb = !preview && mc.options.keyUse.isDown();
		
		drawKey("LMB", rx, ry + (bw + gap) * 2, mbw, bw, lmb);
		drawKey("RMB", rx + mbw + gap, ry + (bw + gap) * 2, mbw, bw, rmb);
	}

	private void drawKey(String key, float x, float y, float w, float h, boolean pressed) {
		int bg = pressed ? UIColors.withAlpha(UIColors.PURE_WHITE, 120) : UIColors.withAlpha(UIColors.PURE_BLACK, 100);
		int fg = pressed ? UIColors.PURE_BLACK : UIColors.PURE_WHITE;
		
		NVGRenderer.rect(x, y, w, h, bg, 4f * scale);
		NVGRenderer.centerText(key, x + w / 2f, y + (h - 11f * scale) / 2f, Fonts.PRETENDARD_SEMIBOLD, fg, 11f * scale);
	}

	@Override
	public float getPreviewWidth() {
		return 64;
	}

	@Override
	public float getPreviewHeight() {
		return 64;
	}
}
