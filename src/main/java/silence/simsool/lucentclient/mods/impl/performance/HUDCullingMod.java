package silence.simsool.lucentclient.mods.impl.performance;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Targets", priority = 900)
public class HUDCullingMod extends Mod {

	public HUDCullingMod() {
		super("HUD Culling", "Lowers the maximum rendering frame rate for HUD elements to save performance.", "Performance", "culling, hud, performance", LucentClientUtils.getModIcon("entity_culling"));
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(HUDCullingMod.class);
	}

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Max Render FPS",
		description = "The target maximum FPS for rendering HUD elements.",
		category = "General",
		min = 5,
		max = 144,
		step = 1,
		priority = 1000
	)
	public static int MaxRenderFPS = 60;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Cull Overlays",
		description = "Cull Pumpkin, freezing, spyglass, portal (except Vignette).",
		category = "Targets",
		priority = 980
	)
	public static boolean CullOverlays = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Cull Hotbar",
		description = "Cull the hotbar.",
		category = "Targets",
		priority = 970
	)
	public static boolean CullHotbar = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Cull Crosshair",
		description = "Cull the crosshair.",
		category = "Targets",
		priority = 960
	)
	public static boolean CullCrosshair = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Cull Bossbars",
		description = "Cull bossbars.",
		category = "Targets",
		priority = 950
	)
	public static boolean CullBossbars = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Cull Debug Screen",
		description = "Cull the F3 debug screen.",
		category = "Targets",
		priority = 940
	)
	public static boolean CullDebugScreen = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Cull Titles",
		description = "Cull titles.",
		category = "Targets",
		priority = 930
	)
	public static boolean CullTitles = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Cull Scoreboard",
		description = "Cull the scoreboard.",
		category = "Targets",
		priority = 920
	)
	public static boolean CullScoreboard = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Cull Chat",
		description = "Cull the chat.",
		category = "Targets",
		priority = 910
	)
	public static boolean CullChat = true;

}