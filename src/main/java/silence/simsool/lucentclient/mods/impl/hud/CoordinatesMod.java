package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.utils.LucentCategory;

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
				"lucent.config.lucentclient.coordinatesmod.general.name", "lucent.config.lucentclient.coordinatesmod.general.description",
				LucentCategory.HUB,
				"location, biome",
				LucentClientUtils.getModIcon("coordinates")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(CoordinatesMod.class);
	}

	@ModConfig(
		type = ConfigType.SELECTOR, 
		name = "lucent.config.lucentclient.coordinatesmod.property.listmode.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.listmode.description",
		options = {"Vertical", "Horizontal", "Simple"}, 
		priority = 1000
	)
	public static String ListMode = "Vertical";

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.coordinatesmod.property.textshadow.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.textshadow.description",
		priority = 930
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "lucent.config.lucentclient.coordinatesmod.property.textcolor.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.textcolor.description",
		priority = 920
	)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.coordinatesmod.property.showbackground.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.showbackground.description",
		priority = 910
	)
	public static boolean ShowBackground = false;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "lucent.config.lucentclient.coordinatesmod.property.backgroundcolor.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.backgroundcolor.description",
		priority = 900
	)
	public static int BackgroundColor = 0x80000000;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.coordinatesmod.property.showx.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.showx.description",
		category = "Coordinates",
		priority = 990
	)
	public static boolean ShowX = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.coordinatesmod.property.showy.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.showy.description",
		category = "Coordinates",
		priority = 980
	)
	public static boolean ShowY = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.coordinatesmod.property.showz.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.showz.description",
		category = "Coordinates",
		priority = 970
	)
	public static boolean ShowZ = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.coordinatesmod.property.showccounter.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.showccounter.description",
		category = "Information",
		priority = 960
	)
	public static boolean ShowCCounter = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.coordinatesmod.property.showdirection.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.showdirection.description",
		category = "Information",
		priority = 950
	)
	public static boolean ShowDirection = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.coordinatesmod.property.showbiome.name", 
		description = "lucent.config.lucentclient.coordinatesmod.property.showbiome.description",
		category = "Information",
		priority = 940
	)
	public static boolean ShowBiome = true;

}