package silence.simsool.lucentclient.huds.impl;

import static silence.simsool.lucent.Lucent.mc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import silence.simsool.lucent.general.enums.Align;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.general.utils.L10n;
import silence.simsool.lucent.general.utils.useful.UDisplay;
import silence.simsool.lucent.ui.font.LucentFont;
import silence.simsool.lucent.ui.utils.UColor;
import silence.simsool.lucent.ui.utils.skija.Fonts;
import silence.simsool.lucent.ui.utils.skija.SkijaRenderer;
import silence.simsool.lucentclient.handler.ServerHandler;
import silence.simsool.lucentclient.mods.impl.hud.PerformanceMod;

public class PerformanceHUD extends LucentHUD {

	public PerformanceHUD() {
		super("lucentclient_performance", PerformanceMod.class, 0.00625f, 0.1537037f, 1.0f, Align.LEFT);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.SKIJA;
	}

	private record Entry(String label, String value) {}
	private record MeasuredEntry(String label, String value, float labelW, float valueW) {}

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
		float sepWidth = SkijaRenderer.textWidth("  |  ", labelFont, labelFs);
		float valuePad = 3f * scale;
		float total = 0f;
		for (int i = 0; i < entries.size(); i++) {
			Entry e = entries.get(i);
			total += SkijaRenderer.textWidth(e.label + " ", labelFont, labelFs) + valuePad;
			total += SkijaRenderer.textWidth(e.value, valueFont, valueFs) + valuePad;
			if (i < entries.size() - 1) {
				total += sepWidth;
			}
		}
		return total;
	}

	@Override
	public float getPreviewWidth() {
		LucentFont labelFont = Fonts.PRETENDARD;
		LucentFont valueFont = Fonts.PRETENDARD_SEMIBOLD;

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
	public void draw(GuiGraphicsExtractor guiGraphics) {
		if (isEditHudOpen || UDisplay.isDebugScreen()) return;
		render(false);
	}

	@Override
	public void preview(GuiGraphicsExtractor guiGraphics) {
		render(true);
	}

	private void render(boolean preview) {
		LucentFont labelFont = Fonts.PRETENDARD;
		LucentFont valueFont = Fonts.PRETENDARD_EXTRABOLD;

		List<Entry> rawEntries = getActiveEntries(preview); if (rawEntries.isEmpty()) return;

		float rx = getRenderX();
		float ry = getRenderY();
		float labelFs = 14f * scale;
		float valueFs = 14f * scale;
		float sepWidth = SkijaRenderer.textWidth("  |  ", labelFont, labelFs);
		float valuePad = 3f * scale;

		List<MeasuredEntry> entries = new ArrayList<>(rawEntries.size());
		float contentW = 0f;
		for (int i = 0; i < rawEntries.size(); i++) {
			Entry e = rawEntries.get(i);
			float lw = SkijaRenderer.textWidth(e.label + " ", labelFont, labelFs);
			float vw = SkijaRenderer.textWidth(e.value, valueFont, valueFs);
			entries.add(new MeasuredEntry(e.label, e.value, lw, vw));
			contentW += lw + valuePad + vw + valuePad;
			if (i < rawEntries.size() - 1) {
				contentW += sepWidth;
			}
		}

		float totalW = contentW + (PerformanceMod.ShowBackground ? 12f * scale : 0f);
		float maxFs = Math.max(labelFs, valueFs);
		float totalH = (PerformanceMod.ShowBackground ? 20f * scale : maxFs);

		if (PerformanceMod.ShowBackground) {
			SkijaRenderer.rect(rx, ry - 1, totalW, totalH, PerformanceMod.BackgroundColor, 4f * scale);
		}

		float currentX = rx + (PerformanceMod.ShowBackground ? 6f * scale : 0f);

		int color = PerformanceMod.TextColor;
		int lineColor = UColor.withAlpha(color, 225);
		boolean shadow = PerformanceMod.TextShadow;

		for (int i = 0; i < entries.size(); i++) {
			MeasuredEntry e = entries.get(i);

			// Draw label
			String labelStr = e.label + " ";
			float labelY = ry + (totalH - labelFs) / 2f;
			if (shadow) SkijaRenderer.textShadow(labelStr, currentX, labelY, labelFont, color, labelFs);
			else SkijaRenderer.text(labelStr, currentX, labelY, labelFont, color, labelFs);
			currentX += e.labelW + valuePad;

			// Draw value
			float valueY = ry + (totalH - valueFs) / 2f;
			if (shadow) SkijaRenderer.textShadow(e.value, currentX, valueY, valueFont, color, valueFs);
			else SkijaRenderer.text(e.value, currentX, valueY, valueFont, color, valueFs);
			currentX += e.valueW + valuePad;

			// Draw separator
			if (i < entries.size() - 1) {
				float sepY = ry + (totalH - labelFs) / 2f;
				if (shadow) SkijaRenderer.textShadow("  |  ", currentX, sepY, labelFont, lineColor, labelFs);
				else SkijaRenderer.text("  |  ", currentX, sepY, labelFont, lineColor, labelFs);
				currentX += sepWidth;
			}
		}
	}

}