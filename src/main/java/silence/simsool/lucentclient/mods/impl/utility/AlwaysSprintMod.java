package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class AlwaysSprintMod extends Mod {

	public AlwaysSprintMod() {
		super(
				"lucent.config.lucentclient.alwayssprintmod.general.name", "lucent.config.lucentclient.alwayssprintmod.general.description",
				LucentCategory.UTILITY,
				"toggle, sneak",
				LucentClientUtils.getModIcon("always_sprint")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(AlwaysSprintMod.class);
	}

}