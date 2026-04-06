package silence.simsool.lucentclient.huds.impl;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.ui.utils.UIColors;
import silence.simsool.lucent.ui.utils.nvg.Fonts;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.mods.impl.hud.DirectionMod;

public class DirectionHUD extends LucentHUD {

	public DirectionHUD() {
		super("direction", DirectionMod.class, 0.5f, 0.05f, 1.0f, HUDAlignment.CENTER);
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
		float fs = 18f * scale;

		Direction dir = preview ? Direction.SOUTH : mc.player.getDirection();
		String text = dir.getName().toUpperCase();
		
		float tw = NVGRenderer.textWidth(text, Fonts.PRETENDARD_SEMIBOLD, fs);
		NVGRenderer.textShadow(text, rx + (getScaledWidth() - tw) / 2f, ry, Fonts.PRETENDARD_SEMIBOLD, UIColors.PURE_WHITE, fs);
	}

	@Override
	public float getPreviewWidth() {
		return 80;
	}

	@Override
	public float getPreviewHeight() {
		return 20;
	}
}