package silence.simsool.lucentclient.huds.impl.info.impl;

import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucentclient.huds.impl.info.AbstractInfoHUD;
import silence.simsool.lucentclient.mods.impl.hud.CPSMod;

public class CPSHUD extends AbstractInfoHUD {

	public CPSHUD() {
		super("lucentclient_cps", CPSMod.class, 0.00625f, 0.13333334f, HUDAlignment.LEFT);
	}

	@Override
	protected String getLabel() {
		return "CPS";
	}

	@Override
	protected String getValue(boolean preview) {
		return String.valueOf(preview ? 6 : CPSMod.getLeftCPS());
	}

	@Override
	protected boolean isReverseOrder() {
		return CPSMod.ReverseOrder;
	}

	@Override
	protected boolean isShowBrackets() {
		return CPSMod.ShowBrackets;
	}

	@Override
	protected boolean isShowShadow() {
		return CPSMod.TextShadow;
	}

	@Override
	protected int getTextColor() {
		return CPSMod.TextColor;
	}

	@Override
	protected boolean isShowBackground() {
		return CPSMod.ShowBackground;
	}

	@Override
	protected int getBackgroundColor() {
		return CPSMod.BackgroundColor;
	}

}