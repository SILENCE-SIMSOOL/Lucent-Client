package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;
@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Text Style", priority = 500)
@ModConfig.CategoryPriority(name = "Background", priority = 100)
public class FPSMod extends Mod {

	public FPSMod() {
		super(
				"FPS", "Displays your current frames per second.",
				"HUD",
				"frames",
				LucentClientUtils.getModIcon("fps")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(FPSMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Brackets", 
		description = "Encloses the FPS value in brackets on the screen.",
		category = "General",
		priority = 2
	)
	public static boolean ShowBrackets = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Reverse Order", 
		description = "Swaps the display order of the FPS label and value on the screen.",
		category = "General",
		priority = 1
	)
	public static boolean ReverseOrder = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Text Shadow", 
		description = "Adds a shadow effect to the FPS text on the screen.",
		category = "Text Style",
		priority = 2
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "Text Color", 
		description = "Sets the color of the FPS text displayed on the screen.",
		category = "Text Style",
		priority = 1
	)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Background", 
		description = "Displays a background box behind the FPS text on the screen.",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = false;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "Background Color", 
		description = "Sets the color of the background box displayed on the screen.",
		category = "Background",
		priority = 1
	)
	public static int BackgroundColor = 0x80000000;

}
