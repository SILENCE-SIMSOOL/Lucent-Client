package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;

public class FullbrightMod extends Mod {

	public FullbrightMod() {
		super(
				"lucent.config.lucentclient.fullbrightmod.general.name", "lucent.config.lucentclient.fullbrightmod.general.description",
				LucentCategory.GRAPHICS,
				"full, bright, light, gamma",
				"\uE90F"
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(FullbrightMod.class);
	}

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.fullbrightmod.property.brightnesslevel.name",
		description = "lucent.config.lucentclient.fullbrightmod.property.brightnesslevel.description",
		min = 0.0, max = 15.0, step = 0.1
	)
	public static float BrightnessLevel = 15.0f;

}