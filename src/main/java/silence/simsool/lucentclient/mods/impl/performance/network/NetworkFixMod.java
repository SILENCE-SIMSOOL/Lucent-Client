package silence.simsool.lucentclient.mods.impl.performance.network;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class NetworkFixMod extends Mod {

	public NetworkFixMod() {
		super(
				"Network Fix", "Optimizes network settings to reduce lag and improve connection stability.",
				"Performance",
				"optimize, internet",
				LucentClientUtils.getModIcon("network_fix")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(NetworkFixMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Immutable Passengers List",
		description = "Uses ImmutableList for entity passengers to optimize memory and network synchronization.",
		category = "Options",
		priority = 1000
	)
	public static boolean ImmutablePassengers = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Fast UTF-8 Encoding",
		description = "Optimizes UTF-8 string encoding to improve network performance and reduce CPU usage.",
		category = "Options"
	)
	public static boolean FastUtf8Encoding = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Fast VarInt Serialization",
		description = "Optimizes VarInt writing and size calculation to speed up packet serialization.",
		category = "Options"
	)
	public static boolean FastVarInt = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Legacy Query Fix",
		description = "Prevents memory leaks by properly clearing buffers when legacy server queries are inactive.",
		category = "Options"
	)
	public static boolean LegacyQueryFix = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Fast Frame Decoding",
		description = "Highly optimizes the VarInt-based frame decoder to speed up incoming packet processing.",
		category = "Options"
	)
	public static boolean FastFrameDecoding = true;

}