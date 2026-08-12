package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class LoadingScreenMod extends Mod {

	public LoadingScreenMod() {
		super(
				"lucent.config.lucentclient.loadingscreenmod.general.name",
				"lucent.config.lucentclient.loadingscreenmod.general.description",
				LucentCategory.GRAPHICS,
				"world, loading, screen, hide, progress",
				LucentClientUtils.getModIcon("loadingscreen")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(LoadingScreenMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.loadingscreenmod.property.hideworldloadingscreen.name",
		description = "lucent.config.lucentclient.loadingscreenmod.property.hideworldloadingscreen.description"
	)
	public static boolean HideWorldLoadingScreen = true;

}
