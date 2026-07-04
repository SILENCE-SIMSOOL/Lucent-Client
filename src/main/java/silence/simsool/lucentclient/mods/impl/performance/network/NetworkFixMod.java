package silence.simsool.lucentclient.mods.impl.performance.network;

import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class NetworkFixMod extends Mod {

	public NetworkFixMod() {
		super(
				"lucent.config.lucentclient.networkfixmod.general.name", "lucent.config.lucentclient.networkfixmod.general.description",
				LucentCategory.PERFORMANCE,
				"network, fix, optimize, internet",
				LucentClientUtils.getModIcon("network_fix")
		);
	}

	public static boolean isEnabled() {
		return true; // Lucent.config.isModuleEnabled(MemoryLeakFixMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.networkfixmod.property.immutablepassengers.name",
		description = "lucent.config.lucentclient.networkfixmod.property.immutablepassengers.description",
		category = "Options",
		priority = 1000
	)
	public static boolean ImmutablePassengers = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.networkfixmod.property.fastutf8encoding.name",
		description = "lucent.config.lucentclient.networkfixmod.property.fastutf8encoding.description",
		category = "Options"
	)
	public static boolean FastUtf8Encoding = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.networkfixmod.property.fastvarint.name",
		description = "lucent.config.lucentclient.networkfixmod.property.fastvarint.description",
		category = "Options"
	)
	public static boolean FastVarInt = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.networkfixmod.property.legacyqueryfix.name",
		description = "lucent.config.lucentclient.networkfixmod.property.legacyqueryfix.description",
		category = "Options"
	)
	public static boolean LegacyQueryFix = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.networkfixmod.property.fastframedecoding.name",
		description = "lucent.config.lucentclient.networkfixmod.property.fastframedecoding.description",
		category = "Options"
	)
	public static boolean FastFrameDecoding = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.networkfixmod.property.nettymemoryoptimize.name",
		description = "lucent.config.lucentclient.networkfixmod.property.nettymemoryoptimize.description",
		category = "Options"
	)
	public static boolean NettyMemoryOptimize = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.networkfixmod.property.leakdetectionoptimize.name",
		description = "lucent.config.lucentclient.networkfixmod.property.leakdetectionoptimize.description",
		category = "Options"
	)
	public static boolean LeakDetectionOptimize = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.networkfixmod.property.fastvarintprepender.name",
		description = "lucent.config.lucentclient.networkfixmod.property.fastvarintprepender.description",
		category = "Options"
	)
	public static boolean FastVarintPrepender = true;

	public static void configureNettyMemory() {
		if (NettyMemoryOptimize) {
			if (System.getProperty("io.netty.allocator.maxOrder") == null) {
	            System.setProperty("io.netty.allocator.maxOrder", "9");
	        }
		}
	}

}