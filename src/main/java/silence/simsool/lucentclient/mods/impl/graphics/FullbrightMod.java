package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class FullbrightMod extends Mod {

	public FullbrightMod() {
		super("Fullbright", "Makes everything fully bright.", "Graphics", "bright, light, gamma", LucentClientUtils.getModIcon("fullbright"));
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(FullbrightMod.class).isEnabled;
	}

}