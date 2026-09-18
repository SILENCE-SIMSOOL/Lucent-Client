package silence.simsool.lucentclient.mods.impl.performance;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class FastRenderMod extends Mod {

	public FastRenderMod() {
		super(
				"lucent.config.lucentclient.fastrendermod.general.name", "lucent.config.lucentclient.fastrendermod.general.description",
				LucentCategory.PERFORMANCE,
				"fast, render, performance, optimize, immediatelyfast",
				LucentClientUtils.getModIcon("entity_culling")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(FastRenderMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.fastrendermod.property.fontatlasresizing.name",
		description = "lucent.config.lucentclient.fastrendermod.property.fontatlasresizing.description"
	)
	public static boolean FontAtlasResizing = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.fastrendermod.property.fasttextlookup.name",
		description = "lucent.config.lucentclient.fastrendermod.property.fasttextlookup.description"
	)
	public static boolean FastTextLookup = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.fastrendermod.property.enhancedbatching.name",
		description = "lucent.config.lucentclient.fastrendermod.property.enhancedbatching.description"
	)
	public static boolean EnhancedBatching = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.fastrendermod.property.skiptextsorting.name",
		description = "lucent.config.lucentclient.fastrendermod.property.skiptextsorting.description"
	)
	public static boolean SkipTextSorting = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.fastrendermod.property.avoidframebufferswitching.name",
		description = "lucent.config.lucentclient.fastrendermod.property.avoidframebufferswitching.description"
	)
	public static boolean AvoidFramebufferSwitching = true;

}