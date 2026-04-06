package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

public class ArmorStatusMod extends Mod {

	public ArmorStatusMod() {
		super("Armor Status", "Displays currently equipped armor durability.", "HUD", "armor, durability, status", "lucid:armor");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(ArmorStatusMod.class).isEnabled;
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Enabled",
		description = "Enable Armor Status HUD.",
		category = "General",
		priority = 1000
	)
	public static boolean Enabled = true;

}