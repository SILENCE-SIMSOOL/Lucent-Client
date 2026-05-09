package silence.simsool.lucentclient.huds.impl.info.impl;

import silence.simsool.lucent.general.enums.Align;
import silence.simsool.lucentclient.handler.ServerHandler;
import silence.simsool.lucentclient.huds.impl.info.AbstractInfoHUD;
import silence.simsool.lucentclient.mods.impl.hud.PingMod;

public class PingHUD extends AbstractInfoHUD {

	public PingHUD() {
		super("lucentclient_ping", PingMod.class, 0.00625f, 0.19444445f, Align.LEFT);
	}

	@Override
	protected String getLabel() {
		return "Ping";
	}

	@Override
	protected String getValue(boolean preview) {
		if (preview) return "24ms";
		else return ServerHandler.getAveragePing() + "ms";
	}

	@Override
	protected boolean isReverseOrder() {
		return PingMod.ReverseOrder;
	}

	@Override
	protected boolean isShowBrackets() {
		return PingMod.ShowBrackets;
	}

	@Override
	protected boolean isShowShadow() {
		return PingMod.TextShadow;
	}

	@Override
	protected int getTextColor() {
		return PingMod.TextColor;
	}

	@Override
	protected boolean isShowBackground() {
		return PingMod.ShowBackground;
	}

	@Override
	protected int getBackgroundColor() {
		return PingMod.BackgroundColor;
	}

}