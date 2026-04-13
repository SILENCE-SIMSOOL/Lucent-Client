package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class KeystrokesMod extends Mod {

	public static boolean isEnabled() {
		return Lucent.config.getModule(KeystrokesMod.class).isEnabled;
	}

	public KeystrokesMod() {
		super("Keystrokes", "Shows your keyboard and mouse clicks.", "HUD", "keys, keyboard, mouse", LucentClientUtils.getModIcon("keystrokes"));
	}

}