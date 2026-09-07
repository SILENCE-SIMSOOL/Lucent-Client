package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "Text Style", priority = 500)
@ModConfig.CategoryPriority(name = "Background", priority = 100)
public class PerformanceMod extends Mod {

	public PerformanceMod() {
		super(
				"lucent.config.lucentclient.performancemod.general.name", "lucent.config.lucentclient.performancemod.general.description",
				LucentCategory.HUB,
				"performance, fps, tps, ping",
				LucentClientUtils.getModIcon("performance")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(PerformanceMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.performancemod.property.showfps.name",
		description = "lucent.config.lucentclient.performancemod.property.showfps.description",
		priority = 3
	)
	public static boolean ShowFPS = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.performancemod.property.showtps.name",
		description = "lucent.config.lucentclient.performancemod.property.showtps.description",
		priority = 2
	)
	public static boolean ShowTPS = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.performancemod.property.showping.name",
		description = "lucent.config.lucentclient.performancemod.property.showping.description",
		priority = 1
	)
	public static boolean ShowPing = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.performancemod.property.textshadow.name",
		description = "lucent.config.lucentclient.performancemod.property.textshadow.description",
		category = "Text Style",
		priority = 2
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "lucent.config.lucentclient.performancemod.property.textcolor.name",
		description = "lucent.config.lucentclient.performancemod.property.textcolor.description",
		category = "Text Style",
		priority = 1
	)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.performancemod.property.showbackground.name",
		description = "lucent.config.lucentclient.performancemod.property.showbackground.description",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = false;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "lucent.config.lucentclient.performancemod.property.backgroundcolor.name",
		description = "lucent.config.lucentclient.performancemod.property.backgroundcolor.description",
		category = "Background",
		priority = 1
	)
	public static int BackgroundColor = 0x80000000;

}
