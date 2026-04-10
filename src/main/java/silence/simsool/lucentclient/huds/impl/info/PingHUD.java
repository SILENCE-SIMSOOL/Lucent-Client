package silence.simsool.lucentclient.huds.impl.info;

import static silence.simsool.lucent.Lucent.mc;

import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucentclient.mods.impl.hud.PingMod;

public class PingHUD extends AbstractInfoHUD {

	public PingHUD() {
		super("info_ping", PingMod.class, 0.02f, 0.11f, HUDAlignment.LEFT);
	}

	@Override protected String getLabel() { return "Ping"; }
	@Override protected String getValue(boolean preview) { 
		int ping = 0;
		if (!preview && mc.getConnection() != null && mc.player != null && mc.getConnection().getPlayerInfo(mc.player.getUUID()) != null) {
			ping = mc.getConnection().getPlayerInfo(mc.player.getUUID()).getLatency();
		} else if (preview) {
			ping = 24;
		}
		return ping + "ms"; 
	}
	@Override protected boolean isReverseOrder() { return PingMod.ReverseOrder; }
	@Override protected boolean isShowBrackets() { return PingMod.ShowBrackets; }
	@Override protected boolean isShowShadow() { return PingMod.TextShadow; }
	@Override protected int getTextColor() { return PingMod.TextColor; }
	@Override protected boolean isShowBackground() { return PingMod.ShowBackground; }
	@Override protected int getBackgroundColor() { return PingMod.BackgroundColor; }

}
