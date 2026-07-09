package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.utils.LucentCategory;

public class LegacySkinFixMod extends Mod {

	public LegacySkinFixMod() {
		super(
				"Legacy Skin Fix",
				"Fixes skin signature verification errors when connecting to certain servers on newer Minecraft versions.",
				LucentCategory.UTILITY,
				"legacy, skin, fix",
				null
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(LegacySkinFixMod.class);
	}

}