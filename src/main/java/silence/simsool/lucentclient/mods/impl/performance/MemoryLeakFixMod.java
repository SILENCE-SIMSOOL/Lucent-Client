package silence.simsool.lucentclient.mods.impl.performance;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class MemoryLeakFixMod extends Mod {

	public MemoryLeakFixMod() {
		super("MemoryLeak Fix", "", "Performance", "", LucentClientUtils.getModIcon("memoryleak_fix"));
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(MemoryLeakFixMod.class).isEnabled;
	}

}