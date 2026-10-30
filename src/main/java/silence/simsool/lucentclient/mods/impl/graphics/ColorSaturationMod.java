package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class ColorSaturationMod extends Mod {

	public ColorSaturationMod() {
		super(
				"lucent.config.lucentclient.colorsaturationmod.general.name", "lucent.config.lucentclient.colorsaturationmod.general.description",
				LucentCategory.GRAPHICS,
				"saturation, color, contrast, brightness, hue",
				LucentClientUtils.getModIcon("color_saturation")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(ColorSaturationMod.class);
	}

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.colorsaturationmod.property.strength.name",
		description = "lucent.config.lucentclient.colorsaturationmod.property.strength.description",
		min = -1.0f, max = 5.0f, step = 0.05f,
		priority = 100
	)
	public static float Strength = 1.0f;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.colorsaturationmod.property.contrast.name",
		description = "lucent.config.lucentclient.colorsaturationmod.property.contrast.description",
		min = 0.0f, max = 2.0f, step = 0.05f,
		priority = 90
	)
	public static float Contrast = 1.0f;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.colorsaturationmod.property.brightness.name",
		description = "lucent.config.lucentclient.colorsaturationmod.property.brightness.description",
		min = 0.0f, max = 2.0f, step = 0.05f,
		priority = 80
	)
	public static float Brightness = 1.0f;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.colorsaturationmod.property.hue.name",
		description = "lucent.config.lucentclient.colorsaturationmod.property.hue.description",
		min = -180.0f, max = 180.0f, step = 1.0f,
		priority = 70
	)
	public static float Hue = 0.0f;

	public static boolean isIdentity() {
		return Math.abs(Strength - 1.0f) < 1e-4f
				&& Math.abs(Contrast - 1.0f) < 1e-4f
				&& Math.abs(Brightness - 1.0f) < 1e-4f
				&& Math.abs(Hue) < 1e-4f;
	}

	public static boolean isActive() {
		return isEnabled() && !isIdentity();
	}

}