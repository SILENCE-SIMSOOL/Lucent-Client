package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class HurtCamMod extends Mod {

	public HurtCamMod() {
		super(
				"lucent.config.lucentclient.hurtcammod.general.name", "lucent.config.lucentclient.hurtcammod.general.description",
				LucentCategory.GRAPHICS,
				"hurt, cam, camera, shake",
				LucentClientUtils.getModIcon("hurtcam")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(HurtCamMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.hurtcammod.property.disablehurtcam.name",
		description = "lucent.config.lucentclient.hurtcammod.property.disablehurtcam.description"
	)
	public static boolean DisableHurtCam = true;

}