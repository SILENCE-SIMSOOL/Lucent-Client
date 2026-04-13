package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class ToggleSprintMod extends Mod {

	public ToggleSprintMod() {
		super("Toggle Sprint", "Allows you to toggle your sprint state.", "Utility", "sprint, run, toggle", LucentClientUtils.getModIcon("toggle_sprint"));
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(ToggleSprintMod.class).isEnabled;
	}

}