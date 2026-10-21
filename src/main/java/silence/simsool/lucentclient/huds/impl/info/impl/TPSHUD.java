package silence.simsool.lucentclient.huds.impl.info.impl;

import silence.simsool.lucent.general.enums.Align;
import silence.simsool.lucentclient.handler.ServerHandler;
import silence.simsool.lucentclient.huds.impl.info.AbstractInfoHUD;
import silence.simsool.lucentclient.mods.impl.hud.TPSMod;

public class TPSHUD extends AbstractInfoHUD {

	public TPSHUD() {
		super("lucentclient_tps", TPSMod.class, 0.00625f, 0.17407407f, Align.LEFT);
	}

	@Override
	protected String getLabel() {
		return "TPS";
	}

	@Override
	protected String getValue(boolean preview) {
		if (preview) return "20.0";
		return ServerHandler.getTextAverageTPS();
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