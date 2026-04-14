package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Coordinates", priority = 500)
@ModConfig.CategoryPriority(name = "Information", priority = 100)
public class CoordinatesMod extends Mod {

	public CoordinatesMod() {
		super(
				"Coordinates", "Displays your current world coordinates and location info.",
				"HUD",
				"location, biome",
				LucentClientUtils.getModIcon("coordinates")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(CoordinatesMod.class);
	}

	@ModConfig(
		type = ConfigType.SELECTOR, 
		name = "List Mode", 
		description = "Changes the display layout of the coordinates on the screen.",
		category = "General",
		options = {"Vertical", "Horizontal", "Simple"}, 
		priority = 1000
	)
	public static String ListMode = "Vertical";

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Text Shadow", 
		description = "Adds a shadow effect to the coordinate text on the screen.",
		category = "General",
		priority = 930
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "Text Color", 
		description = "Sets the color of the coordinate text displayed on the screen.",
		category = "General",
		priority = 920
	)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Background", 
		description = "Displays a background box behind the coordinate text on the screen.",
		category = "General",
		priority = 910
	)
	public static boolean ShowBackground = false;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "Background Color", 
		description = "Sets the color of the background box displayed on the screen.",
		category = "General",
		priority = 900
	)
	public static int BackgroundColor = 0x80000000;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show X", 
		description = "Displays the X-axis coordinate on the screen.",
		category = "Coordinates",
		priority = 990
	)
	public static boolean ShowX = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Y", 
		description = "Displays the Y-axis coordinate on the screen.",
		category = "Coordinates",
		priority = 980
	)
	public static boolean ShowY = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Z", 
		description = "Displays the Z-axis coordinate on the screen.",
		category = "Coordinates",
		priority = 970
	)
	public static boolean ShowZ = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show C Counter", 
		description = "Displays the chunk-relative coordinate (C counter) on the screen.",
		category = "Information",
		priority = 960
	)
	public static boolean ShowCCounter = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Direction", 
		description = "Displays the direction you are facing on the screen.",
		category = "Information",
		priority = 950
	)
	public static boolean ShowDirection = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Biome", 
		description = "Displays the current biome name on the screen.",
		category = "Information",
		priority = 940
	)
	public static boolean ShowBiome = true;

}
