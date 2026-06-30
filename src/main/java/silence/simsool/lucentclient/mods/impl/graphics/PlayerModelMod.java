package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "Armor Model", priority = 1000)
@ModConfig.CategoryPriority(name = "Player Model", priority = 900)
public class PlayerModelMod extends Mod {

	public PlayerModelMod() {
		super(
				"lucent.config.lucentclient.playermodelmod.general.name", "lucent.config.lucentclient.playermodelmod.general.description",
				LucentCategory.GRAPHICS,
				"player, model, armor",
				LucentClientUtils.getModIcon("player_model")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(PlayerModelMod.class);
	}

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmor.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmor.description",
			category = "Armor Model",
			priority = 1000
	)
	public static boolean HideArmor = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorself.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorself.description",
			category = "Armor Model",
			parent = "HideArmor",
			priority = 990
	)
	public static boolean HideArmorSelf = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorothers.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorothers.description",
			category = "Armor Model",
			parent = "HideArmor",
			priority = 980
	)
	public static boolean HideArmorOthers = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmoronlyplayer.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmoronlyplayer.description",
			category = "Armor Model",
			parent = "HideArmor",
			priority = 970
	)
	public static boolean HideArmorOnlyPlayer = true;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorselfhelmet.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorselfhelmet.description",
			category = "Armor Model",
			parent = "HideArmorSelf",
			priority = 960
	)
	public static boolean HideArmorSelfHelmet = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorselfchestplate.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorselfchestplate.description",
			category = "Armor Model",
			parent = "HideArmorSelf",
			priority = 950
	)
	public static boolean HideArmorSelfChestplate = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorselfleggings.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorselfleggings.description",
			category = "Armor Model",
			parent = "HideArmorSelf",
			priority = 940
	)
	public static boolean HideArmorSelfLeggings = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorselfboots.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorselfboots.description",
			category = "Armor Model",
			parent = "HideArmorSelf",
			priority = 930
	)
	public static boolean HideArmorSelfBoots = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorselfskull.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorselfskull.description",
			category = "Armor Model",
			parent = "HideArmorSelf",
			priority = 920
	)
	public static boolean HideArmorSelfSkull = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorothershelmet.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorothershelmet.description",
			category = "Armor Model",
			parent = "HideArmorOthers",
			priority = 910
	)
	public static boolean HideArmorOthersHelmet = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorotherschestplate.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorotherschestplate.description",
			category = "Armor Model",
			parent = "HideArmorOthers",
			priority = 900
	)
	public static boolean HideArmorOthersChestplate = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorothersleggings.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorothersleggings.description",
			category = "Armor Model",
			parent = "HideArmorOthers",
			priority = 890
	)
	public static boolean HideArmorOthersLeggings = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorothersboots.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorothersboots.description",
			category = "Armor Model",
			parent = "HideArmorOthers",
			priority = 880
	)
	public static boolean HideArmorOthersBoots = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "lucent.config.lucentclient.playermodelmod.property.hidearmorothersskull.name",
			description = "lucent.config.lucentclient.playermodelmod.property.hidearmorothersskull.description",
			category = "Armor Model",
			parent = "HideArmorOthers",
			priority = 870
	)
	public static boolean HideArmorOthersSkull = false;

	@ModConfig(
			type = ConfigType.SLIDER,
			name = "lucent.config.lucentclient.playermodelmod.property.playerscale.name",
			description = "lucent.config.lucentclient.playermodelmod.property.playerscale.description",
			category = "Player Model",
			min = 0.0, max = 2.0, step = 0.1,
			priority = 800
	)
	public static double PlayerScale = 1.0;

}