package silence.simsool.lucentclient.mods.impl.performance;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class MemoryLeakFixMod extends Mod {

	public MemoryLeakFixMod() {
		super(
				"lucent.config.lucentclient.memoryleakfixmod.general.name", "lucent.config.lucentclient.memoryleakfixmod.general.description",
				LucentCategory.PERFORMANCE,
				"optimize",
				LucentClientUtils.getModIcon("memoryleak_fix")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(MemoryLeakFixMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.staticbiomecache.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.staticbiomecache.description"
	)
	public static boolean StaticBiomeCache = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.targetcleanup.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.targetcleanup.description"
	)
	public static boolean TargetCleanup = true;

}