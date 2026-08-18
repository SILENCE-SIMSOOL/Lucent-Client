package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class TimeChangerMod extends Mod {

	public TimeChangerMod() {
		super(
				"lucent.config.lucentclient.timechangermod.general.name", "lucent.config.lucentclient.timechangermod.general.description",
				LucentCategory.GRAPHICS,
				"time, changer, day, night",
				LucentClientUtils.getModIcon("time_changer")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(TimeChangerMod.class);
	}

	@ModConfig(
		type = ConfigType.SELECTOR,
		name = "lucent.config.lucentclient.timechangermod.property.timeselection.name",
		description = "lucent.config.lucentclient.timechangermod.property.timeselection.description",
		options = {"Off", "Day", "Noon", "Sunset", "Night", "Midnight", "Custom"},
		priority = 1000
	)
	public static String TimeSelection = "Off";

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.timechangermod.property.customtimevalue.name",
		description = "lucent.config.lucentclient.timechangermod.property.customtimevalue.description",
		parent = "TimeSelection",
		selector = "Custom",
		min = 0,
		max = 24000,
		step = 100,
		priority = 990
	)
	public static double CustomTimeValue = 6000;

}