package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;

public class BetterNightVisionMod extends Mod {

	public BetterNightVisionMod() {
		super(
				"lucent.config.lucentclient.betternightvisionmod.general.name",
				"lucent.config.lucentclient.betternightvisionmod.general.description",
				LucentCategory.GRAPHICS,
				"night, vision, better, cleaner",
				null
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(BetterNightVisionMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.betternightvisionmod.property.cleanernightvision.name",
		description = "lucent.config.lucentclient.betternightvisionmod.property.cleanernightvision.description"
	)
	public static boolean CleanerNightVision = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.betternightvisionmod.property.disablenightvision.name",
		description = "lucent.config.lucentclient.betternightvisionmod.property.disablenightvision.description"
	)
	public static boolean DisableNightVision = false;
}