package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;
@ModConfig.CategoryPriority(name = "Text Style", priority = 500)
@ModConfig.CategoryPriority(name = "Background", priority = 100)
public class FPSMod extends Mod {

	public FPSMod() {
		super(
				"lucent.config.lucentclient.fpsmod.general.name", "lucent.config.lucentclient.fpsmod.general.description",
				LucentCategory.HUB,
				"fps, frames",
				LucentClientUtils.getModIcon("fps")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(FPSMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.fpsmod.property.showbrackets.name", 
		description = "lucent.config.lucentclient.fpsmod.property.showbrackets.description",
		priority = 2
	)
	public static boolean ShowBrackets = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.fpsmod.property.reverseorder.name", 
		description = "lucent.config.lucentclient.fpsmod.property.reverseorder.description",
		priority = 1
	)
	public static boolean ReverseOrder = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.fpsmod.property.textshadow.name", 
		description = "lucent.config.lucentclient.fpsmod.property.textshadow.description",
		category = "Text Style",
		priority = 2
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "lucent.config.lucentclient.fpsmod.property.textcolor.name", 
		description = "lucent.config.lucentclient.fpsmod.property.textcolor.description",
		category = "Text Style",
		priority = 1
	)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.fpsmod.property.showbackground.name", 
		description = "lucent.config.lucentclient.fpsmod.property.showbackground.description",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = false;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "lucent.config.lucentclient.fpsmod.property.backgroundcolor.name", 
		description = "lucent.config.lucentclient.fpsmod.property.backgroundcolor.description",
		category = "Background",
		priority = 1
	)
	public static int BackgroundColor = 0x80000000;

}