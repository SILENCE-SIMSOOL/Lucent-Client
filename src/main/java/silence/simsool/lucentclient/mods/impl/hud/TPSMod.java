package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Text Style", priority = 500)
@ModConfig.CategoryPriority(name = "Background", priority = 100)
public class TPSMod extends Mod {

	public TPSMod() {
		super(
				"lucent.config.lucentclient.tpsmod.general.name", "lucent.config.lucentclient.tpsmod.general.description",
				LucentCategory.HUB,
				"tps, ticks",
				LucentClientUtils.getModIcon("tps")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(TPSMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.tpsmod.property.showbrackets.name", 
		description = "lucent.config.lucentclient.tpsmod.property.showbrackets.description",
		priority = 2
	)
	public static boolean ShowBrackets = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.tpsmod.property.reverseorder.name", 
		description = "lucent.config.lucentclient.tpsmod.property.reverseorder.description",
		priority = 1
	)
	public static boolean ReverseOrder = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.tpsmod.property.textshadow.name", 
		description = "lucent.config.lucentclient.tpsmod.property.textshadow.description",
		category = "Text Style",
		priority = 2
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "lucent.config.lucentclient.tpsmod.property.textcolor.name", 
		description = "lucent.config.lucentclient.tpsmod.property.textcolor.description",
		category = "Text Style",
		priority = 1
	)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.tpsmod.property.showbackground.name", 
		description = "lucent.config.lucentclient.tpsmod.property.showbackground.description",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = false;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "lucent.config.lucentclient.tpsmod.property.backgroundcolor.name", 
		description = "lucent.config.lucentclient.tpsmod.property.backgroundcolor.description",
		category = "Background",
		priority = 1
	)
	public static int BackgroundColor = 0x80000000;

}