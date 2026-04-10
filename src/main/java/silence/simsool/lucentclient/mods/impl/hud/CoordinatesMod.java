package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

@ModConfig.CategoryPriority(name = "Coordinates", priority = 1000)
public class CoordinatesMod extends Mod {

	public CoordinatesMod() {
		super("Coordinates", "Displays your current world coordinates and location info.", "HUD", "coordinates, coord, location, biome", "lucid:info");
	}

	@ModConfig(type = ConfigType.SELECTOR, name = "List Mode", options = {"Vertical", "Horizontal", "Simple"}, priority = 1000)
	public static String ListMode = "Vertical";

	@ModConfig(type = ConfigType.SWITCH, name = "Show X", priority = 990)
	public static boolean ShowX = true;

	@ModConfig(type = ConfigType.SWITCH, name = "Show Y", priority = 980)
	public static boolean ShowY = true;

	@ModConfig(type = ConfigType.SWITCH, name = "Show Z", priority = 970)
	public static boolean ShowZ = true;

	@ModConfig(type = ConfigType.SWITCH, name = "Show C Counter", priority = 960)
	public static boolean ShowCCounter = true;

	@ModConfig(type = ConfigType.SWITCH, name = "Show Direction", priority = 950)
	public static boolean ShowDirection = true;

	@ModConfig(type = ConfigType.SWITCH, name = "Show Biome", priority = 940)
	public static boolean ShowBiome = true;

	@ModConfig(type = ConfigType.SWITCH, name = "Text Shadow", priority = 930)
	public static boolean TextShadow = true;

	@ModConfig(type = ConfigType.COLOR, name = "Text Color", priority = 920)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(type = ConfigType.SWITCH, name = "Show Background", priority = 910)
	public static boolean ShowBackground = false;

	@ModConfig(type = ConfigType.COLOR, name = "Background Color", priority = 900)
	public static int BackgroundColor = 0x80000000;

}
