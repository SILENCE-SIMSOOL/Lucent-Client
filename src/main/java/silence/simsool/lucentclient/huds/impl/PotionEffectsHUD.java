package silence.simsool.lucentclient.huds.impl;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;
import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.general.utils.UDisplay;
import silence.simsool.lucent.ui.utils.UIColors;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.mods.impl.hud.PotionEffectsMod;

public class PotionEffectsHUD extends LucentHUD {

	public PotionEffectsHUD() {
		super("potion_effects", PotionEffectsMod.class, 0.95f, 0.05f, 1.0f, HUDAlignment.RIGHT);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.MINECRAFT;
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
		
		int sw = mc.getWindow().getGuiScaledWidth();
		int sh = mc.getWindow().getGuiScaledHeight();

		float rx = x * sw;
		float ry = y * sh;
		
		if (alignment == HUDAlignment.CENTER) rx -= (getScaledWidth() / 2f);
		else if (alignment == HUDAlignment.RIGHT) rx -= getScaledWidth();
		
		float lineH = 10f * scale;
		int yOffset = 0;

		if (preview) {
			drawText(guiGraphics, "Speed II : 1:30", rx, ry + yOffset);
			yOffset += lineH;
			drawText(guiGraphics, "Strength : 0:45", rx, ry + yOffset);
		} else {
			for (MobEffectInstance effect : mc.player.getActiveEffects()) {
				String name = effect.getEffect().value().getDescriptionId(); // Should ideally be translated
				String duration = StringUtil.formatTickDuration(effect.getDuration(), mc.level.tickRateManager().tickrate());
				String text = name + " " + (effect.getAmplifier() + 1) + " : " + duration;
				drawText(guiGraphics, text, rx, ry + yOffset);
				yOffset += lineH;
			}
		}
	}

	private void drawText(GuiGraphics guiGraphics, String text, float x, float y) {
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().translate(Math.round(x), Math.round(y));
		guiGraphics.pose().scale(scale, scale);
		guiGraphics.drawString(mc.font, text, 0, 0, UIColors.PURE_WHITE, true);
		guiGraphics.pose().popMatrix();
	}

	@Override
	public float getPreviewWidth() {
		return 100 * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
	}

	@Override
	public float getPreviewHeight() {
		return 40 * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
	}
}
