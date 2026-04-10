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
		name = "Helmet",
		description = "",
		category = "Setup",
		priority = 6
	)
	public static boolean DisplayHelmet = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Chestplate",
		description = "",
		category = "Setup",
		priority = 5
	)
	public static boolean DisplayChestplate = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Leggings",
		description = "",
		category = "Setup",
		priority = 4
	)
	public static boolean DisplayLeggings = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Boots",
		description = "",
		category = "Setup",
		priority = 3
	)
	public static boolean DisplayBoots = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Main Hand",
		description = "",
		category = "Setup",
		priority = 2
	)
	public static boolean DisplayMainHand = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Off Hand",
		description = "",
		category = "Setup",
		priority = 1
	)
	public static boolean DisplayOffHand = false;
}