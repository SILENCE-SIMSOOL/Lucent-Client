package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class ScrollableTooltipsMod extends Mod {

	public ScrollableTooltipsMod() {
		super("Scrollable Tooltips", "Allows you to scroll long item tooltips.", "Utility", "scroll, tooltip, item", LucentClientUtils.getModIcon("scrollable_tooltips"));
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(ScrollableTooltipsMod.class).isEnabled;
	}

}