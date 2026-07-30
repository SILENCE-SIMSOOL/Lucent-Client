package silence.simsool.lucentclient.huds.impl;

import static silence.simsool.lucent.Lucent.mc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;
import silence.simsool.lucent.general.enums.Align;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.general.utils.useful.UDisplay;
import silence.simsool.lucent.general.utils.useful.UText;
import silence.simsool.lucent.ui.utils.UIColors;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.mods.impl.hud.PotionEffectsMod;

public class PotionEffectsHUD extends LucentHUD {

	public PotionEffectsHUD() {
		super("lucentclient_potioneffects", PotionEffectsMod.class, 0.005f, 0.22777778f, 1.0f, Align.LEFT);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.MINECRAFT;
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && PotionEffectsMod.ShowHUD;
	}

	@Override
	public void disable() {
		PotionEffectsMod.ShowHUD = false;
	}

	@Override
	public float getPreviewWidth() {
		List<String> lines = getLines(true);
		float maxW = 50;
		for (String line : lines) maxW = Math.max(maxW, mc.font.width(line));
		return maxW * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
	}

	@Override
	public float getPreviewHeight() {
		List<String> lines = getLines(true);
		float h = lines.isEmpty() ? 10 : lines.size() * 10;
		return h * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
	}

	@Override
	public void draw(GuiGraphicsExtractor guiGraphics) {
		if (isEditHudOpen || UDisplay.isDebugScreen()) return;
		render(guiGraphics, false);
	}

	@Override
	public void preview(GuiGraphicsExtractor guiGraphics) {
		render(guiGraphics, true);
	}

	private void render(GuiGraphicsExtractor guiGraphics, boolean preview) {
		List<String> lines = getLines(preview);
		if (lines.isEmpty()) return;

		int sw = UDisplay.getGuiScaledWidth();
		int sh = UDisplay.getGuiScaledHeight();

		float rx = x * sw;
		float ry = y * sh;

		float maxW = 0;
		for (String line : lines) maxW = Math.max(maxW, mc.font.width(line));

		float scaledW = maxW * scale;

		if (alignment == Align.CENTER) rx -= (scaledW / 2f);
		else if (alignment == Align.RIGHT) rx -= scaledW;
		
		float lineH = 10f * scale;
		int yOffset = 0;

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			float lineW = mc.font.width(line);
			float lx = rx;
			
			if (alignment == Align.CENTER) lx += (maxW - lineW) * scale / 2f;
			else if (alignment == Align.RIGHT) lx += (maxW - lineW) * scale;
			
			UText.drawText(guiGraphics, line, lx, ry + yOffset, scale, UIColors.PURE_WHITE, true);
			yOffset += lineH;
		}
	}

	private List<String> getLines(boolean preview) {
		List<String> lines = new ArrayList<>();
		if (preview) {
			lines.add("Speed II : 1:30");
			lines.add("Strength : 0:45");
		} else {
			if (mc.player == null) return lines;
			for (MobEffectInstance effect : mc.player.getActiveEffects()) {
				String name = Component.translatable(effect.getEffect().value().getDescriptionId()).getString();
				
				String amplifier = "";
				int lvl = effect.getAmplifier();
				if (lvl >= 0 && lvl < 10) {
					String[] roman = {"", " II", " III", " IV", " V", " VI", " VII", " VIII", " IX", " X"};
					amplifier = roman[lvl];
				} else {
					amplifier = " " + (lvl + 1);
				}

				String duration = StringUtil.formatTickDuration(effect.getDuration(), mc.level.tickRateManager().tickrate());
				lines.add(name + amplifier + " : " + duration);
			}
		}
		return lines;
	}

}