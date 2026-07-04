package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;

public class LegacySkinFix extends Mod {

	public LegacySkinFix() {
		super("lucent.config.lucentclient.legacyskinfix.general.name", "lucent.config.lucentclient.legacyskinfix.general.description", "fixer", null, null);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(LegacySkinFix.class);
	}

}