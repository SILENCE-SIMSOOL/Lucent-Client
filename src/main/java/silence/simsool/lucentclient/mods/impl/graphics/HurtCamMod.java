package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class HurtCamMod extends Mod {

	public HurtCamMod() {
		super(
				"Hurt Cam", "Modify the camera shaking when taking damage.",
				"Graphics",
				"hurt, camera, shake",
				LucentClientUtils.getModIcon("hurtcam")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(HurtCamMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Disable Hurt Cam",
		description = "Completely disables the camera shake effect when taking damage.",
		category = "General"
	)
	public static boolean DisableHurtCam = true;

}