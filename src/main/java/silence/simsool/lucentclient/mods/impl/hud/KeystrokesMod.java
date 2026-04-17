package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class KeystrokesMod extends Mod {

	public KeystrokesMod() {
		super(
				"Keystrokes", "Shows your keyboard and mouse clicks.",
				"HUD",
				"mouse, click",
				LucentClientUtils.getModIcon("keystrokes")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(KeystrokesMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Show Mouse Buttons",
		description = "Displays left and right mouse clicks on the screen.",
		category = "General",
		priority = 2
	)
	public static boolean ShowMouseButtons = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Show Spacebar",
		description = "Displays spacebar clicks on the screen.",
		category = "General",
		priority = 1
	)
	public static boolean ShowSpacebar = false;

}