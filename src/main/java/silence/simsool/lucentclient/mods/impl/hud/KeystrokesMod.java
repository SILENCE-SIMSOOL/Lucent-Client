package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class KeystrokesMod extends Mod {

	public KeystrokesMod() {
		super(
				"lucent.config.lucentclient.keystrokesmod.general.name", "lucent.config.lucentclient.keystrokesmod.general.description",
				LucentCategory.HUB,
				"key, stroke, keyboard, mouse, click",
				LucentClientUtils.getModIcon("keystrokes")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(KeystrokesMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.keystrokesmod.property.showmousebuttons.name",
		description = "lucent.config.lucentclient.keystrokesmod.property.showmousebuttons.description",
		priority = 2
	)
	public static boolean ShowMouseButtons = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.keystrokesmod.property.showspacebar.name",
		description = "lucent.config.lucentclient.keystrokesmod.property.showspacebar.description",
		priority = 1
	)
	public static boolean ShowSpacebar = false;

}