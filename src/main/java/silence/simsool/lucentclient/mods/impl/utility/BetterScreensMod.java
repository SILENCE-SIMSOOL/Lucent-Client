package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;

public class BetterScreensMod extends Mod {

	public BetterScreensMod() {
		super(
				"lucent.config.lucentclient.betterscreensmod.general.name",
				"lucent.config.lucentclient.betterscreensmod.general.description",
				LucentCategory.UTILITY,
				"better, screens, cursor, recipe, container",
				"\uEF5B"
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(BetterScreensMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.betterscreensmod.property.preventclosingscreens.name",
		description = "lucent.config.lucentclient.betterscreensmod.property.preventclosingscreens.description"
	)
	public static boolean PreventClosingScreens = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.betterscreensmod.property.dontresetcursor.name",
		description = "lucent.config.lucentclient.betterscreensmod.property.dontresetcursor.description"
	)
	public static boolean DontResetCursor = true;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.betterscreensmod.property.cursorresettimer.name",
		description = "lucent.config.lucentclient.betterscreensmod.property.cursorresettimer.description",
		min = 0, max = 2000, step = 50
	)
	public static int CursorResetTimer = 200;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.betterscreensmod.property.removerecipebookshift.name",
		description = "lucent.config.lucentclient.betterscreensmod.property.removerecipebookshift.description"
	)
	public static boolean RemoveRecipeBookShift = false;

}