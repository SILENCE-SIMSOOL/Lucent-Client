package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;

public class LegacySkinFix extends Mod {

	public LegacySkinFix() {
		super("Legacy Skin Fix", "Fixes skin signature verification errors when connecting to certain servers on newer Minecraft versions.", "fixer", null, null);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(LegacySkinFix.class);
	}

}