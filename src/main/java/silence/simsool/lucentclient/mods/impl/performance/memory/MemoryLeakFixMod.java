package silence.simsool.lucentclient.mods.impl.performance.memory;

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
		return true; // return Lucent.config.isModuleEnabled(MemoryLeakFixMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.neighborlookup.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.neighborlookup.description"
	)
	public static boolean FakeReplaceNeighborLookup = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.propertymap.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.propertymap.description"
	)
	public static boolean FakeReplacePropertyMap = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.predicates.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.predicates.description"
	)
	public static boolean FakeCacheMultipartPredicates = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.mrlcache.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.mrlcache.description"
	)
	public static boolean FakeModelResourceLocations = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.dedupmultipart.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.dedupmultipart.description"
	)
	public static boolean FakeMultipartDeduplication = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.dedupblockstate.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.dedupblockstate.description"
	)
	public static boolean FakeBlockstateCacheDeduplication = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.dedupquads.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.dedupquads.description"
	)
	public static boolean FakeBakedQuadDeduplication = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.modelsides.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.modelsides.description"
	)
	public static boolean FakeModelSides = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.datacomponents.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.datacomponents.description"
	)
	public static boolean FakeDataComponentPatch = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.threaddetector.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.threaddetector.description"
	)
	public static boolean FakeUseSmallThreadingDetector = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.compactfastmap.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.compactfastmap.description"
	)
	public static boolean FakeCompactFastMap = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.memoryleakfixmod.property.populateneighbor.name",
		description = "lucent.config.lucentclient.memoryleakfixmod.property.populateneighbor.description"
	)
	public static boolean FakePopulateNeighborTable = true;

	public static final boolean ReplaceNeighborLookup = true;
	public static final boolean ReplacePropertyMap = true;
	public static final boolean CacheMultipartPredicates = true;
	public static final boolean ModelResourceLocations = true;
	public static final boolean MultipartDeduplication = true;
	public static final boolean BlockstateCacheDeduplication = true;
	public static final boolean BakedQuadDeduplication = true;
	public static final boolean ModelSides = true;
	public static final boolean DataComponentPatch = true;
	public static final boolean UseSmallThreadingDetector = true;
	public static final boolean CompactFastMap = true;
	public static final boolean PopulateNeighborTable = true;

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