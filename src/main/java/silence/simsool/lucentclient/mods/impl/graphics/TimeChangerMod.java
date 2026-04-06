package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class TimeChangerMod extends Mod {

	public TimeChangerMod() {
		super("Time Changer", "Allows you to set a fixed client-side time.", "Graphics", "time, day, night", "lucid:clock");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(TimeChangerMod.class).isEnabled;
	}

	@ModConfig(
		type = ConfigType.SELECTOR,
		name = "Time Selection",
		description = "Select a preset time or use custom value.",
		category = "General",
		options = {"Off", "Day", "Noon", "Sunset", "Night", "Midnight", "Custom"},
		priority = 1000
	)
	public static String TimeSelection = "Off";

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Custom Time",
		description = "Manual time value when 'Custom' is selected.",
		category = "General",
		parent = "timeSelection",
		min = 0,
		max = 24000,
		step = 100,
		priority = 990
	)
	public static double CustomTimeValue = 6000;

}