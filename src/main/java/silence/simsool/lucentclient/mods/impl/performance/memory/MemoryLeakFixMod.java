package silence.simsool.lucentclient.mods.impl.performance.memory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucentclient.ducks.IPlatform;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class MemoryLeakFixMod extends Mod {

	public MemoryLeakFixMod() {
		super(
				"lucent.config.lucentclient.memoryleakfixmod.general.name", "lucent.config.lucentclient.memoryleakfixmod.general.description",
				LucentCategory.PERFORMANCE,
				"memory, leak, fix, performance, optimize",
				LucentClientUtils.getModIcon("memoryleak_fix")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(MemoryLeakFixMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.neighborlookup.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.neighborlookup.description"
	)
	public static boolean ReplaceNeighborLookup = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.propertymap.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.propertymap.description"
	)
	public static boolean ReplacePropertyMap = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.predicates.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.predicates.description"
	)
	public static boolean CacheMultipartPredicates = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.mrlcache.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.mrlcache.description"
	)
	public static boolean ModelResourceLocations = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.dedupmultipart.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.dedupmultipart.description"
	)
	public static boolean MultipartDeduplication = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.dedupblockstate.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.dedupblockstate.description"
	)
	public static boolean BlockstateCacheDeduplication = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.dedupquads.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.dedupquads.description"
	)
	public static boolean BakedQuadDeduplication = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.modelsides.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.modelsides.description"
	)
	public static boolean ModelSides = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.datacomponents.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.datacomponents.description"
	)
	public static boolean DataComponentPatch = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.threaddetector.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.threaddetector.description"
	)
	public static boolean UseSmallThreadingDetector = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.compactfastmap.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.compactfastmap.description"
	)
	public static boolean CompactFastMap = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.populateneighbor.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.populateneighbor.description"
	)
	public static boolean PopulateNeighborTable = true;

	public static final IPlatform PLATFORM_HOOKS;

	static {
		try {
			Class<?> hooks = Class.forName("silence.simsool.lucentclient.hooks.PlatformHook");
			PLATFORM_HOOKS = (IPlatform) hooks.getConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

}