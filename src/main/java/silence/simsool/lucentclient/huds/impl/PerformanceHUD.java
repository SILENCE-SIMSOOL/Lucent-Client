package silence.simsool.lucentclient.huds.impl;

import static silence.simsool.lucent.Lucent.mc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiGraphics;
import silence.simsool.lucent.general.enums.Align;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.general.utils.L10n;
import silence.simsool.lucent.general.utils.useful.UDisplay;
import silence.simsool.lucent.ui.font.LucentFont;
import silence.simsool.lucent.ui.utils.UColor;
import silence.simsool.lucent.ui.utils.nvg.Fonts;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.handler.ServerHandler;
import silence.simsool.lucentclient.mods.impl.hud.PerformanceMod;

public class PerformanceHUD extends LucentHUD {

	public PerformanceHUD() {
		super("lucentclient_performance", PerformanceMod.class, 0.00625f, 0.1537037f, 1.0f, Align.LEFT);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.NANOVG;
	}

	private record Entry(String label, String value) {}

	private List<Entry> getActiveEntries(boolean preview) {
		List<Entry> entries = new ArrayList<>();
		if (PerformanceMod.ShowFPS) {
			entries.add(new Entry("FPS", String.valueOf(preview ? 144 : mc.getFps())));
		}
		if (PerformanceMod.ShowTPS) {
			entries.add(new Entry("TPS", preview ? "20.0" : ServerHandler.getTextAverageTPS()));
		}
		if (PerformanceMod.ShowPing) {
			String pingLabel = L10n.translate("lucent.hud.performance.ping");
			entries.add(new Entry(pingLabel, (preview ? 0 : ServerHandler.getAveragePing()) + " ms"));
		}
		return entries;
	}

	private float calculateContentWidth(List<Entry> entries, LucentFont labelFont, LucentFont valueFont, float labelFs, float valueFs, float scale) {
		if (entries.isEmpty()) return 0f;
		float sepWidth = NVGRenderer.textWidth("  |  ", labelFont, labelFs);
		float valuePad = 3f * scale;
		float total = 0f;
		for (int i = 0; i < entries.size(); i++) {
			Entry e = entries.get(i);
			total += NVGRenderer.textWidth(e.label + " ", labelFont, labelFs) + valuePad;
			total += NVGRenderer.textWidth(e.value, valueFont, valueFs) + valuePad;
			if (i < entries.size() - 1) {
				total += sepWidth;
			}
		}
		return total;
	}

	@Override
	public float getPreviewWidth() {
		LucentFont labelFont = Fonts.PRETENDARD != null ? Fonts.PRETENDARD : Fonts.PRETENDARD_MEDIUM;
		LucentFont valueFont = Fonts.PRETENDARD_SEMIBOLD != null ? Fonts.PRETENDARD_SEMIBOLD : labelFont;

		List<Entry> entries = getActiveEntries(true);
		float labelFs = 14f;
		float valueFs = 14f;
		float w = calculateContentWidth(entries, labelFont, valueFont, labelFs, valueFs, 1.0f);
		if (PerformanceMod.ShowBackground) {
			w += 12f;
		}
		return Math.max(10f, w);
	}

	@Override
	public float getPreviewHeight() {
		float labelFs = 14f;
		float valueFs = 14f;
		float maxFs = Math.max(labelFs, valueFs);
		return PerformanceMod.ShowBackground ? 20f : maxFs;
	}

	@Override
	public void draw(GuiGraphics guiGraphics) {
		if (isEditHudOpen || UDisplay.isDebugScreen()) return;
		render(false);
	}

	@Override
	public void preview(GuiGraphics guiGraphics) {
		render(true);
	}

	private void render(boolean preview) {
		LucentFont labelFont = Fonts.PRETENDARD_MEDIUM;
		LucentFont valueFont = Fonts.PRETENDARD_EXTRABOLD;

		List<Entry> entries = getActiveEntries(preview); if (entries.isEmpty()) return;

		float rx = getRenderX();
		float ry = getRenderY();
		float labelFs = 14f * scale;
		float valueFs = 14f * scale;
		float sepWidth = NVGRenderer.textWidth("  |  ", labelFont, labelFs);

		float contentW = calculateContentWidth(entries, labelFont, valueFont, labelFs, valueFs, scale);
		float totalW = contentW + (PerformanceMod.ShowBackground ? 12f * scale : 0f);
		float maxFs = Math.max(labelFs, valueFs);
		float totalH = (PerformanceMod.ShowBackground ? 20f * scale : maxFs);

		if (PerformanceMod.ShowBackground) {
			NVGRenderer.rect(rx, ry - 1, totalW, totalH, PerformanceMod.BackgroundColor, 4f * scale);
		}

		float currentX = rx + (PerformanceMod.ShowBackground ? 6f * scale : 0f);

		int color = PerformanceMod.TextColor;
		int lineColor = UColor.withAlpha(color, 225);
		boolean shadow = PerformanceMod.TextShadow;

		float valuePad = 3f * scale;

		for (int i = 0; i < entries.size(); i++) {
			Entry e = entries.get(i);

			// Draw label
			String labelStr = e.label + " ";
			float labelY = ry + (totalH - labelFs) / 2f;
			if (shadow) NVGRenderer.textShadow(labelStr, currentX, labelY, labelFont, color, labelFs);
			else NVGRenderer.text(labelStr, currentX, labelY, labelFont, color, labelFs);
			currentX += NVGRenderer.textWidth(labelStr, labelFont, labelFs) + valuePad;

			// Draw value
			float valueY = ry + (totalH - valueFs) / 2f;
			if (shadow) NVGRenderer.textShadow(e.value, currentX, valueY, valueFont, color, valueFs);
			else NVGRenderer.text(e.value, currentX, valueY, valueFont, color, valueFs);
			currentX += NVGRenderer.textWidth(e.value, valueFont, valueFs) + valuePad;

			// Draw separator
			if (i < entries.size() - 1) {
				float sepY = ry + (totalH - labelFs) / 2f;
				if (shadow) NVGRenderer.textShadow("  |  ", currentX, sepY, labelFont, lineColor, labelFs);
				else NVGRenderer.text("  |  ", currentX, sepY, labelFont, lineColor, labelFs);
				currentX += sepWidth;
			}
		}
	}

}