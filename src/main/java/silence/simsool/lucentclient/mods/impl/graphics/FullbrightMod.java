package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class FullbrightMod extends Mod {

	public FullbrightMod() {
		super(
				"Fullbright", "Makes everything fully bright.",
				"Graphics",
				"light, gamma",
				LucentClientUtils.getModIcon("fullbright")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(FullbrightMod.class);
	}

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Brightness Level",
		description = "Sets the gamma level.",
		category = "General",
		min = 0.0, max = 15.0, step = 0.1
	)
	public static float BrightnessLevel = 15.0f;

}