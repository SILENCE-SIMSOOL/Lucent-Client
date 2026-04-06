package silence.simsool.lucentclient.huds.impl;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;
import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.ui.utils.UIColors;
import silence.simsool.lucent.ui.utils.nvg.Fonts;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.mods.impl.hud.PotionEffectsMod;

public class PotionEffectsHUD extends LucentHUD {

	public PotionEffectsHUD() {
		super("potion_effects", PotionEffectsMod.class, 0.95f, 0.05f, 1.0f, HUDAlignment.RIGHT);
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
		float fs = 13f * scale;
		float lineH = 15f * scale;
		
		int yOffset = 0;

		if (preview) {
			NVGRenderer.textShadow("Speed II : 1:30", rx, ry, Fonts.PRETENDARD_MEDIUM, UIColors.PURE_WHITE, fs);
			NVGRenderer.textShadow("Strength : 0:45", rx, ry + lineH, Fonts.PRETENDARD_MEDIUM, UIColors.PURE_WHITE, fs);
		} else {
			for (MobEffectInstance effect : mc.player.getActiveEffects()) {
				String name = effect.getEffect().value().getDescriptionId(); // Should ideally be translated
				String duration = StringUtil.formatTickDuration(effect.getDuration(), mc.level.tickRateManager().tickrate());
				String text = name + " " + (effect.getAmplifier() + 1) + " : " + duration;
				NVGRenderer.textShadow(text, rx, ry + yOffset, Fonts.PRETENDARD_MEDIUM, UIColors.PURE_WHITE, fs);
				yOffset += lineH;
			}
		}
	}

	@Override
	public float getPreviewWidth() {
		return 120;
	}

	@Override
	public float getPreviewHeight() {
		return 40;
	}
}
