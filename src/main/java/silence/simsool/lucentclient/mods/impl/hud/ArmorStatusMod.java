package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class ArmorStatusMod extends Mod {

	public ArmorStatusMod() {
		super(
				"Armor Status", "Displays currently equipped armor durability.",
				"HUD",
				"armor, durability, status",
				LucentClientUtils.getModIcon("armor_status")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(ArmorStatusMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Helmet",
		description = "Displays the durability and status of the helmet on the screen.",
		category = "Armor",
		priority = 6
	)
	public static boolean DisplayHelmet = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Chestplate",
		description = "Displays the durability and status of the chestplate on the screen.",
		category = "Armor",
		priority = 5
	)
	public static boolean DisplayChestplate = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Leggings",
		description = "Displays the durability and status of the leggings on the screen.",
		category = "Armor",
		priority = 4
	)
	public static boolean DisplayLeggings = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Boots",
		description = "Displays the durability and status of the boots on the screen.",
		category = "Armor",
		priority = 3
	)
	public static boolean DisplayBoots = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Main Hand",
		description = "Displays the durability of the item in your main hand on the screen.",
		category = "Item",
		priority = 2
	)
	public static boolean DisplayMainHand = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Off Hand",
		description = "Displays the durability of the item in your off hand on the screen.",
		category = "Item",
		priority = 1
	)
	public static boolean DisplayOffHand = false;

}
