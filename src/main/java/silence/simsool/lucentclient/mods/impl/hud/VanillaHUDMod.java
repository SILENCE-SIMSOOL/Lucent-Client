package silence.simsool.lucentclient.mods.impl.hud;

import java.awt.Color;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Bars", priority = 900)
@ModConfig.CategoryPriority(name = "Scoreboard", priority = 800)
@ModConfig.CategoryPriority(name = "Attack Indicator", priority = 700)
public class VanillaHUDMod extends Mod {

	public VanillaHUDMod() {
		super("Vanilla HUD", "Configure default Minecraft HUD elements.", "HUD", "vanilla, hud, bar", LucentClientUtils.getModIcon("vanillahud"));
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(VanillaHUDMod.class).isEnabled;
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Health Bar",
		description = "Toggle the visibility of your health bar.",
		category = "Bars",
		priority = 1000
	)
	public static boolean HealthBar = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Hunger Bar",
		description = "Toggle the visibility of your hunger bar.",
		category = "Bars",
		priority = 990
	)
	public static boolean HungerBar = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Armor Bar",
		description = "Toggle the visibility of your armor bar.",
		category = "Bars",
		priority = 980
	)
	public static boolean ArmorBar = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Boss Bar",
		description = "Toggle the visibility of the boss health bars.",
		category = "Bars",
		priority = 970
	)
	public static boolean BossBar = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Boss Bar Shadow",
		description = "Toggle text shadow for the boss health bar names.",
		category = "Bars",
		parent = "bossBar",
		priority = 960
	)
	public static boolean BossBarShadow = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Scoreboard",
		description = "Toggle the visibility of the sidebar scoreboard.",
		category = "Scoreboard",
		priority = 800
	)
	public static boolean Scoreboard = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Scoreboard Shadow",
		description = "Toggle text shadow for the scoreboard entries.",
		category = "Scoreboard",
		parent = "scoreboard",
		priority = 795
	)
	public static boolean ScoreboardShadow = true;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "Title BG Color",
		description = "Background color for the scoreboard title.",
		category = "Scoreboard",
		parent = "scoreboard",
		priority = 790
	)
	public static Color ScoreboardTitleColor = new Color(0, 0, 0, 80);

	@ModConfig(
		type = ConfigType.COLOR,
		name = "Content BG Color",
		description = "Background color for the scoreboard entries.",
		category = "Scoreboard",
		parent = "scoreboard",
		priority = 785
	)
	public static Color ScoreboardContentColor = new Color(0, 0, 0, 60);

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Block Breaking",
		description = "Toggle attack indicator for block breaking.",
		category = "Attack Indicator",
		priority = 700
	)
	public static boolean IndicatorBlockBreaking = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Ranged Weapon Draw",
		description = "Toggle attack indicator for bow/crossbow charging.",
		category = "Attack Indicator",
		priority = 690
	)
	public static boolean IndicatorRangedDraw = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Eating & Drinking",
		description = "Toggle attack indicator when eating or drinking.",
		category = "Attack Indicator",
		priority = 680
	)
	public static boolean IndicatorEatingDrinking = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Item Cooldowns",
		description = "Toggle attack indicator for item cooldowns (like ender pearls).",
		category = "Attack Indicator",
		priority = 670
	)
	public static boolean IndicatorItemCooldowns = true;

}