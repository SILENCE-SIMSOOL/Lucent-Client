package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class HurtCamMod extends Mod {

	public HurtCamMod() {
		super("Hurt Cam", "Modify the camera shaking when taking damage.", "Graphics", "hurt, camera, shake", LucentClientUtils.getModIcon("hurtcam"));
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(HurtCamMod.class).isEnabled;
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "No Hurt Cam",
		description = "Disables camera shaking when taking damage.",
		category = "General",
		priority = 1000
	)
	public static boolean NoHurtCam = true;

}
