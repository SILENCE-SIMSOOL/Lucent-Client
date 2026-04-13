package silence.simsool.lucentclient.mods.impl.performance;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class NetworkFixMod extends Mod {

	public NetworkFixMod() {
		super("Network Fix", "", "Performance", "", LucentClientUtils.getModIcon("network_fix"));
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(NetworkFixMod.class).isEnabled;
	}

}