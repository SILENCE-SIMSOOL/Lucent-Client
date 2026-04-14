package silence.simsool.lucentclient.huds.impl.info.impl;

import static silence.simsool.lucent.Lucent.mc;

import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucentclient.huds.impl.info.AbstractInfoHUD;
import silence.simsool.lucentclient.mods.impl.hud.FPSMod;

public class FPSHUD extends AbstractInfoHUD {

	public FPSHUD() {
		super("lucentclient_fps", FPSMod.class, 0.005f, 0.166f, HUDAlignment.LEFT);
	}

	@Override
	protected String getLabel() {
		return "FPS";
	}

	@Override
	protected String getValue(boolean preview) {
		return String.valueOf(preview ? 144 : mc.getFps());
	}

	@Override
	protected boolean isReverseOrder() {
		return FPSMod.ReverseOrder;
	}

	@Override
	protected boolean isShowBrackets() {
		return FPSMod.ShowBrackets;
	}

	@Override
	protected boolean isShowShadow() {
		return FPSMod.TextShadow;
	}

	@Override
	protected int getTextColor() {
		return FPSMod.TextColor;
	}

	@Override
	protected boolean isShowBackground() {
		return FPSMod.ShowBackground;
	}

	@Override
	protected int getBackgroundColor() {
		return FPSMod.BackgroundColor;
	}

}