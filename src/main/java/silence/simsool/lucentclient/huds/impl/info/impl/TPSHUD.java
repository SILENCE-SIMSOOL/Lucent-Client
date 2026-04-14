package silence.simsool.lucentclient.huds.impl.info.impl;

import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucentclient.handler.ServerHandler;
import silence.simsool.lucentclient.huds.impl.info.AbstractInfoHUD;
import silence.simsool.lucentclient.mods.impl.hud.TPSMod;

public class TPSHUD extends AbstractInfoHUD {

	public TPSHUD() {
		super("lucentclient_tps", TPSMod.class, 0.005f, 0.194f, HUDAlignment.LEFT);
	}

	@Override
	protected String getLabel() {
		return "TPS";
	}

	@Override
	protected String getValue(boolean preview) {
		if (preview) return "20.0";
		return ServerHandler.getTextTPS();
	}

	@Override
	protected boolean isReverseOrder() {
		return TPSMod.ReverseOrder;
	}

	@Override
	protected boolean isShowBrackets() {
		return TPSMod.ShowBrackets;
	}

	@Override
	protected boolean isShowShadow() {
		return TPSMod.TextShadow;
	}

	@Override
	protected int getTextColor() {
		return TPSMod.TextColor;
	}

	@Override
	protected boolean isShowBackground() {
		return TPSMod.ShowBackground;
	}

	@Override
	protected int getBackgroundColor() {
		return TPSMod.BackgroundColor;
	}

}