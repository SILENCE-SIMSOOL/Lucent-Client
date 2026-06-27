package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfigExtra;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class ArmorStatusMod extends Mod {

	public ArmorStatusMod() {
		super(
				"lucent.config.lucentclient.armorstatusmod.general.name", "lucent.config.lucentclient.armorstatusmod.general.description",
				LucentCategory.HUB,
				"armor, durability, status",
				LucentClientUtils.getModIcon("armor_status")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(ArmorStatusMod.class);
	}

	@ModConfigExtra(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.armorstatusmod.property.displayhelmet.name",
		description = "lucent.config.lucentclient.armorstatusmod.property.displayhelmet.description",
		category = "Armor",
		forcewidget = true,
		priority = 6
	)
	public static boolean DisplayHelmet = true;

	@ModConfigExtra(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.armorstatusmod.property.displaychestplate.name",
		description = "lucent.config.lucentclient.armorstatusmod.property.displaychestplate.description",
		category = "Armor",
		forcewidget = true,
		priority = 5
	)
	public static boolean DisplayChestplate = true;

	@ModConfigExtra(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.armorstatusmod.property.displayleggings.name",
		description = "lucent.config.lucentclient.armorstatusmod.property.displayleggings.description",
		category = "Armor",
		forcewidget = true,
		priority = 4
	)
	public static boolean DisplayLeggings = true;

	@ModConfigExtra(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.armorstatusmod.property.displayboots.name",
		description = "lucent.config.lucentclient.armorstatusmod.property.displayboots.description",
		category = "Armor",
		forcewidget = true,
		priority = 3
	)
	public static boolean DisplayBoots = true;

	@ModConfigExtra(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.armorstatusmod.property.displaymainhand.name",
		description = "lucent.config.lucentclient.armorstatusmod.property.displaymainhand.description",
		category = "Item",
		forcewidget = true,
		priority = 2
	)
	public static boolean DisplayMainHand = false;

	@ModConfigExtra(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.armorstatusmod.property.displayoffhand.name",
		description = "lucent.config.lucentclient.armorstatusmod.property.displayoffhand.description",
		category = "Item",
		forcewidget = true,
		priority = 1
	)
	public static boolean DisplayOffHand = false;

}