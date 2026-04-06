package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

public class DirectionMod extends Mod {

	public DirectionMod() {
		super("Direction HUD", "Displays your looking direction.", "HUD", "compass, direction", "lucid:compass");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(DirectionMod.class).isEnabled;
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Enabled",
		description = "Enable Direction HUD.",
		category = "General",
		priority = 1000
	)
	public static boolean Enabled = true;

}