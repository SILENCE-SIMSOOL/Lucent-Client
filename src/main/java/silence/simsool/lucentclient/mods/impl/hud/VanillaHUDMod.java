package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.utils.LucentCategory;

import java.awt.Color;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "Bars", priority = 900)
@ModConfig.CategoryPriority(name = "Crosshair", priority = 900)
@ModConfig.CategoryPriority(name = "Scoreboard", priority = 800)
@ModConfig.CategoryPriority(name = "Potion", priority = 700)
@ModConfig.CategoryPriority(name = "Attack Indicator", priority = 600)
public class VanillaHUDMod extends Mod {

	public VanillaHUDMod() {
		super(
				"lucent.config.lucentclient.vanillahudmod.general.name", "lucent.config.lucentclient.vanillahudmod.general.description",
				LucentCategory.HUB,
				"bar, health, hunger, armor, boss, scoreboard, indicator, crosshair, potion, effect",
				LucentClientUtils.getModIcon("vanillahud")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(VanillaHUDMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.healthbar.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.healthbar.description",
		category = "Bars",
		priority = 1000
	)
	public static boolean HealthBar = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.hungerbar.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.hungerbar.description",
		category = "Bars",
		priority = 990
	)
	public static boolean HungerBar = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.armorbar.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.armorbar.description",
		category = "Bars",
		priority = 980
	)
	public static boolean ArmorBar = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.bossbar.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.bossbar.description",
		category = "Bars",
		priority = 970
	)
	public static boolean BossBar = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.bossbarshadow.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.bossbarshadow.description",
		category = "Bars",
		parent = "BossBar",
		priority = 960
	)
	public static boolean BossBarShadow = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.thirdpersoncrosshair.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.thirdpersoncrosshair.description",
		category = "Crosshair"
	)
	public static boolean ThirdPersonCrosshair = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.scoreboard.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.scoreboard.description",
		category = "Scoreboard",
		priority = 1000
	)
	public static boolean Scoreboard = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.scoreboardshadow.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.scoreboardshadow.description",
		category = "Scoreboard",
		parent = "Scoreboard",
		priority = 900
	)
	public static boolean ScoreboardShadow = true;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "lucent.config.lucentclient.vanillahudmod.property.scoreboardtitlecolor.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.scoreboardtitlecolor.description",
		category = "Scoreboard",
		parent = "Scoreboard",
		priority = 800
	)
	public static Color ScoreboardTitleColor = new Color(0, 0, 0, 80);

	@ModConfig(
		type = ConfigType.COLOR,
		name = "lucent.config.lucentclient.vanillahudmod.property.scoreboardcontentcolor.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.scoreboardcontentcolor.description",
		category = "Scoreboard",
		parent = "Scoreboard",
		priority = 700
	)
	public static Color ScoreboardContentColor = new Color(0, 0, 0, 60);

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.potioneffects.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.potioneffects.description",
		category = "Potion"
	)
	public static boolean PotionEffects = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.indicatorblockbreaking.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.indicatorblockbreaking.description",
		category = "Attack Indicator",
		priority = 700
	)
	public static boolean IndicatorBlockBreaking = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.indicatorrangeddraw.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.indicatorrangeddraw.description",
		category = "Attack Indicator",
		priority = 1000
	)
	public static boolean IndicatorRangedDraw = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.indicatoreatingdrinking.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.indicatoreatingdrinking.description",
		category = "Attack Indicator",
		priority = 900
	)
	public static boolean IndicatorEatingDrinking = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.vanillahudmod.property.indicatoritemcooldowns.name",
		description = "lucent.config.lucentclient.vanillahudmod.property.indicatoritemcooldowns.description",
		category = "Attack Indicator",
		priority = 800
	)
	public static boolean IndicatorItemCooldowns = true;

}