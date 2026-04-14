package silence.simsool.lucentclient.mods.impl.graphics;

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
				"Player Model", "Modify your player and armor model.",
				"Graphics",
				"armor",
				LucentClientUtils.getModIcon("player_model")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(PlayerModelMod.class);
	}

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Armor",
			description = "Prevents rendering of selectable armor pieces.",
			category = "Armor Model",
			priority = 1000
	)
	public static boolean HideArmor = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Armor Self",
			description = "Hide the armor of yourself.",
			category = "Armor Model",
			parent = "HideArmor",
			priority = 990
	)
	public static boolean HideArmorSelf = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Armor Others",
			description = "Hide the armor of others.",
			category = "Armor Model",
			parent = "HideArmor",
			priority = 980
	)
	public static boolean HideArmorOthers = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Armor Only Player",
			description = "Only hide armor on players.",
			category = "Armor Model",
			parent = "HideArmor",
			priority = 970
	)
	public static boolean HideArmorOnlyPlayer = true;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Self Helmet",
			description = "Hide your helmet.",
			category = "Armor Model",
			parent = "HideArmorSelf",
			priority = 960
	)
	public static boolean HideArmorSelfHelmet = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Self Chestplate",
			description = "Hide your chestplate.",
			category = "Armor Model",
			parent = "HideArmorSelf",
			priority = 950
	)
	public static boolean HideArmorSelfChestplate = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Self Leggings",
			description = "Hide your leggings.",
			category = "Armor Model",
			parent = "HideArmorSelf",
			priority = 940
	)
	public static boolean HideArmorSelfLeggings = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Self Boots",
			description = "Hide your boots.",
			category = "Armor Model",
			parent = "HideArmorSelf",
			priority = 930
	)
	public static boolean HideArmorSelfBoots = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Self Skull",
			description = "Hide your skull.",
			category = "Armor Model",
			parent = "HideArmorSelf",
			priority = 920
	)
	public static boolean HideArmorSelfSkull = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Others Helmet",
			description = "Hide other players' helmets.",
			category = "Armor Model",
			parent = "HideArmorOthers",
			priority = 910
	)
	public static boolean HideArmorOthersHelmet = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Others Chestplate",
			description = "Hide other players' chestplates.",
			category = "Armor Model",
			parent = "HideArmorOthers",
			priority = 900
	)
	public static boolean HideArmorOthersChestplate = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Others Leggings",
			description = "Hide other players' leggings.",
			category = "Armor Model",
			parent = "HideArmorOthers",
			priority = 890
	)
	public static boolean HideArmorOthersLeggings = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Others Boots",
			description = "Hide other players' boots.",
			category = "Armor Model",
			parent = "HideArmorOthers",
			priority = 880
	)
	public static boolean HideArmorOthersBoots = false;

	@ModConfig(
			type = ConfigType.SWITCH,
			name = "Hide Others Skull",
			description = "Hide other players' skulls.",
			category = "Armor Model",
			parent = "HideArmorOthers",
			priority = 870
	)
	public static boolean HideArmorOthersSkull = false;

	@ModConfig(
			type = ConfigType.SLIDER,
			name = "Player Scale",
			description = "Changes player scale. default: 1",
			category = "Player Model",
			min = 0.0, max = 2.0, step = 0.1,
			priority = 800
	)
	public static double PlayerScale = 1.0;

}

