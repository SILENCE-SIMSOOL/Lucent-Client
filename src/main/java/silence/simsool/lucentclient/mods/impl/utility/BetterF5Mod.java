package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class BetterF5Mod extends Mod {

	public BetterF5Mod() {
		super(
				"lucent.config.lucentclient.betterf5mod.general.name", "lucent.config.lucentclient.betterf5mod.general.description",
				LucentCategory.UTILITY,
				"f5, better, camera, perspective",
				LucentClientUtils.getModIcon("betterf5")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(BetterF5Mod.class);
	}

}