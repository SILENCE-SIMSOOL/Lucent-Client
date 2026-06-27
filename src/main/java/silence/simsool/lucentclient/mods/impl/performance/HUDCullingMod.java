package silence.simsool.lucentclient.mods.impl.performance;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Targets", priority = 900)
public class HUDCullingMod extends Mod {

	public HUDCullingMod() {
		super(
				"lucent.config.lucentclient.hudcullingmod.general.name", "lucent.config.lucentclient.hudcullingmod.general.description",
				LucentCategory.PERFORMANCE, "culling, hud, performance", LucentClientUtils.getModIcon("entity_culling"));
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(HUDCullingMod.class);
	}

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.hudcullingmod.property.maxrenderfps.name",
		description = "lucent.config.lucentclient.hudcullingmod.property.maxrenderfps.description",
		min = 5,
		max = 144,
		step = 1,
		priority = 1000
	)
	public static int MaxRenderFPS = 60;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.hudcullingmod.property.culloverlays.name",
		description = "lucent.config.lucentclient.hudcullingmod.property.culloverlays.description",
		category = "Targets",
		priority = 980
	)
	public static boolean CullOverlays = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.hudcullingmod.property.cullhotbar.name",
		description = "lucent.config.lucentclient.hudcullingmod.property.cullhotbar.description",
		category = "Targets",
		priority = 970
	)
	public static boolean CullHotbar = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.hudcullingmod.property.cullcrosshair.name",
		description = "lucent.config.lucentclient.hudcullingmod.property.cullcrosshair.description",
		category = "Targets",
		priority = 960
	)
	public static boolean CullCrosshair = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.hudcullingmod.property.cullbossbars.name",
		description = "lucent.config.lucentclient.hudcullingmod.property.cullbossbars.description",
		category = "Targets",
		priority = 950
	)
	public static boolean CullBossbars = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.hudcullingmod.property.culldebugscreen.name",
		description = "lucent.config.lucentclient.hudcullingmod.property.culldebugscreen.description",
		category = "Targets",
		priority = 940
	)
	public static boolean CullDebugScreen = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.hudcullingmod.property.culltitles.name",
		description = "lucent.config.lucentclient.hudcullingmod.property.culltitles.description",
		category = "Targets",
		priority = 930
	)
	public static boolean CullTitles = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.hudcullingmod.property.cullscoreboard.name",
		description = "lucent.config.lucentclient.hudcullingmod.property.cullscoreboard.description",
		category = "Targets",
		priority = 920
	)
	public static boolean CullScoreboard = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.hudcullingmod.property.cullchat.name",
		description = "lucent.config.lucentclient.hudcullingmod.property.cullchat.description",
		category = "Targets",
		priority = 910
	)
	public static boolean CullChat = true;

}