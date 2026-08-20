package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.utils.LucentCategory;

public class AlwaysSprintMod extends Mod {

	public AlwaysSprintMod() {
		super(
				"lucent.config.lucentclient.alwayssprintmod.general.name", "lucent.config.lucentclient.alwayssprintmod.general.description",
				LucentCategory.UTILITY,
				"always, sprint, toggle, sneak",
				"\uE566"
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(AlwaysSprintMod.class);
	}

}