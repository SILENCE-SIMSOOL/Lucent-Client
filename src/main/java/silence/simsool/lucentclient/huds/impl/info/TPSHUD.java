package silence.simsool.lucentclient.huds.impl.info;

import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucentclient.mods.impl.hud.TPSMod;

public class TPSHUD extends AbstractInfoHUD {

	public TPSHUD() {
		super("info_tps", TPSMod.class, 0.02f, 0.08f, HUDAlignment.LEFT);
	}

	@Override protected String getLabel() { return "TPS"; }
	@Override protected String getValue(boolean preview) { return "20.0"; }
	@Override protected boolean isReverseOrder() { return TPSMod.ReverseOrder; }
	@Override protected boolean isShowBrackets() { return TPSMod.ShowBrackets; }
	@Override protected boolean isShowShadow() { return TPSMod.TextShadow; }
	@Override protected int getTextColor() { return TPSMod.TextColor; }
	@Override protected boolean isShowBackground() { return TPSMod.ShowBackground; }
	@Override protected int getBackgroundColor() { return TPSMod.BackgroundColor; }

}
