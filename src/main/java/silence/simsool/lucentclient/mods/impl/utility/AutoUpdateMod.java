package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucent.general.utils.useful.UChat;
import silence.simsool.lucentclient.LucentClient;
import silence.simsool.lucentclient.updater.AutoUpdater;

public class AutoUpdateMod extends Mod {

	public AutoUpdateMod() {
		super(
				"Auto Update", "Automatically check for updates and update Lucent Client.",
				LucentCategory.UTILITY,
				"auto, update, latest",
				null,
				true
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(AutoUpdateMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Auto Update", description = "Automatically check for updates and download the latest mod file."
	)
	public static boolean AutoUpdate = true;

	@Override
	public void onWorldLoad() {
		if (AutoUpdate && AutoUpdater.nextUpdate) {
			AutoUpdater.nextUpdate = false;
			UChat.chat("\n " + LucentClient.PREFIX + " §aA new version is available. §7Restart the game to apply the update.\n");
		}
	}

}