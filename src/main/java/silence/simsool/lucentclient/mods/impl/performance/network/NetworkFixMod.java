package silence.simsool.lucentclient.mods.impl.performance.network;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
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
		return Lucent.config.isModuleEnabled(NetworkFixMod.class);
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

}