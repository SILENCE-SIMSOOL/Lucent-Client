package silence.simsool.lucentclient.huds.impl;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.ui.utils.UIColors;
import silence.simsool.lucent.ui.utils.nvg.Fonts;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.mods.impl.hud.InfoHUDMod;

public class InfoHUD extends LucentHUD {

	public InfoHUD() {
		super("info", InfoHUDMod.class, 0.02f, 0.02f, 1.0f, HUDAlignment.LEFT);
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
		float fs = 14f * scale;
		float lineH = 16f * scale;
		
		int yOffset = 0;

		if (preview || InfoHUDMod.ShowFps) {
			String text = "FPS: " + (preview ? "144" : mc.getFps());
			NVGRenderer.textShadow(text, rx, ry + yOffset, Fonts.PRETENDARD_MEDIUM, UIColors.PURE_WHITE, fs);
			yOffset += lineH;
		}

		if (preview || InfoHUDMod.ShowCoordinates) {
			BlockPos pos = preview ? new BlockPos(100, 64, -200) : mc.player.blockPosition();
			String text = String.format("XYZ: %d, %d, %d", pos.getX(), pos.getY(), pos.getZ());
			NVGRenderer.textShadow(text, rx, ry + yOffset, Fonts.PRETENDARD_MEDIUM, UIColors.PURE_WHITE, fs);
			yOffset += lineH;
		}

		if (preview || InfoHUDMod.ShowPing) {
			int ping = 0;
			if (!preview && mc.getConnection() != null && mc.getConnection().getPlayerInfo(mc.player.getUUID()) != null) {
				ping = mc.getConnection().getPlayerInfo(mc.player.getUUID()).getLatency();
			}
			String text = "Ping: " + (preview ? "24" : ping) + "ms";
			NVGRenderer.textShadow(text, rx, ry + yOffset, Fonts.PRETENDARD_MEDIUM, UIColors.PURE_WHITE, fs);
			yOffset += lineH;
		}

		if (preview || InfoHUDMod.ShowCps) {
			NVGRenderer.textShadow("CPS: 0", rx, ry + yOffset, Fonts.PRETENDARD_MEDIUM, UIColors.PURE_WHITE, fs);
			yOffset += lineH;
		}

		if (preview || InfoHUDMod.ShowTps) {
			NVGRenderer.textShadow("TPS: 20.0", rx, ry + yOffset, Fonts.PRETENDARD_MEDIUM, UIColors.PURE_WHITE, fs);
			yOffset += lineH;
		}
	}

	@Override
	public float getPreviewWidth() {
		return 120;
	}

	@Override
	public float getPreviewHeight() {
		return 85;
	}
}