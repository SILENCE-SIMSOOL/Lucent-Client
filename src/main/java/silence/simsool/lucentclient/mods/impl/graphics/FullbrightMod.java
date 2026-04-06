package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;

public class FullbrightMod extends Mod {

	public FullbrightMod() {
		super("Fullbright", "Makes everything fully bright.", "Graphics", "bright, light, gamma", "lucid:sun");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(FullbrightMod.class).isEnabled;
	}

}