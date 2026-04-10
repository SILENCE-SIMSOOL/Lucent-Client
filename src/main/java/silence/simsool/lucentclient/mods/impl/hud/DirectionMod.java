package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;

public class DirectionMod extends Mod {

	public DirectionMod() {
		super("Direction HUD", "Displays your looking direction.", "HUD", "compass, direction", "lucid:compass");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(DirectionMod.class).isEnabled;
	}

}