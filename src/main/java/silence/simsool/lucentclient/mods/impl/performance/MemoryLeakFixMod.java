package silence.simsool.lucentclient.mods.impl.performance;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class MemoryLeakFixMod extends Mod {

	public MemoryLeakFixMod() {
		super(
				"MemoryLeak Fix", "Fixes various memory leaks in the game to maintain long-term stability.",
				"Performance",
				"optimize",
				LucentClientUtils.getModIcon("memoryleak_fix")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(MemoryLeakFixMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Static Biome Cache",
		description = "Uses a static ThreadLocal for biome temperature caching to prevent memory leaks during world transitions.",
		category = "General"
	)
	public static boolean StaticBiomeCache = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Target Entity Cleanup",
		description = "Clears crosshair target data every tick to prevent entity references from being held in memory.",
		category = "General"
	)
	public static boolean TargetCleanup = true;

}
