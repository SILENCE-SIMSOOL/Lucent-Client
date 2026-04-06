package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;

public class ScrollableTooltipsMod extends Mod {

	public ScrollableTooltipsMod() {
		super("Scrollable Tooltips", "Allows you to scroll long item tooltips.", "Utility", "scroll, tooltip, item", "lucid:tooltip");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(ScrollableTooltipsMod.class).isEnabled;
	}

}