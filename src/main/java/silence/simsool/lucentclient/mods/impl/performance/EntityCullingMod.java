package silence.simsool.lucentclient.mods.impl.performance;

import static silence.simsool.lucent.Lucent.mc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucent.general.utils.useful.UWorld;
import silence.simsool.lucentclient.mods.impl.performance.culling.CullTask;
import silence.simsool.lucentclient.mods.impl.performance.culling.CullingDataProvider;
import silence.simsool.lucentclient.mods.impl.performance.culling.OcclusionCullingInstance;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class EntityCullingMod extends Mod {

	public EntityCullingMod() {
		super(
				"lucent.config.lucentclient.entitycullingmod.general.name", "lucent.config.lucentclient.entitycullingmod.general.description",
				LucentCategory.PERFORMANCE,
				"entity, culling, performance, optimize",
				LucentClientUtils.getModIcon("entity_culling")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(EntityCullingMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.cullplayers.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.cullplayers.description"
	)
	public static boolean CullPlayers = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.cullentities.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.cullentities.description"
	)
	public static boolean CullEntities = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.culldroppeditems.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.culldroppeditems.description"
	)
	public static boolean CullDroppedItems = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.cullblockentities.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.cullblockentities.description"
	)
	public static boolean CullBlockEntities = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.blockentityfrustumculling.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.blockentityfrustumculling.description"
	)
	public static boolean BlockEntityFrustumCulling = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.tickculling.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.tickculling.description"
	)
	public static boolean TickCulling = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.solidleaves.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.solidleaves.description"
	)
	public static boolean SolidLeaves = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.forcedisplayculling.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.forcedisplayculling.description"
	)
	public static boolean ForceDisplayCulling = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.rendernametagsthroughwalls.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.rendernametagsthroughwalls.description"
	)
	public static boolean RenderNametagsThroughWalls = false;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.entitycullingmod.property.tracingdistance.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.tracingdistance.description",
		min = 32, max = 256, step = 16
	)
	public static int TracingDistance = 128;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.entitycullingmod.property.hitboxlimit.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.hitboxlimit.description",
		min = 10, max = 100, step = 5
	)
	public static int HitboxLimit = 50;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.entitycullingmod.property.sleepdelay.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.sleepdelay.description",
		min = 1, max = 50, step = 1
	)
	public static int SleepDelay = 10;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.showdebuginfo.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.showdebuginfo.description",
		category = "Debug"
	)
	public static boolean ShowDebugInfo = true;

	public static Frustum frustum = null;

	public static final List<Predicate<Entity>> IGNORE_FILTERS = new CopyOnWriteArrayList<>();
	public static final List<Predicate<BlockEntity>> BLOCK_ENTITY_IGNORE_FILTERS = new CopyOnWriteArrayList<>();

	public static final Set<String> BLOCK_ENTITY_WHITELIST_STRINGS = new HashSet<>(Arrays.asList(
			"betterend:eternal_pedestal",
			"botania:falling_star",
			"botania:flame_ring",
			"botania:magic_missile",
			"create:hose_pulley",
			"create:rope_pulley",
			"minecraft:beacon"
	));

	public static final Set<String> ENTITY_WHITELIST_STRINGS = new HashSet<>(Arrays.asList(
			"botania:mana_burst",
			"drg_flares:drg_flares",
			"quark:soul_bead"
	));

	public static final Set<String> TICK_CULLING_WHITELIST_STRINGS = new HashSet<>(Arrays.asList(
			"alexscaves:gum_worm",
			"alexscaves:gum_worm_segment",
			"avm_staff:campfire_flame",
			"cinematiccataclysm:ancient_remnant_cutscene",
			"cinematiccataclysm:ignis_cutscene",
			"cinematiccataclysm:leviathan_cutscene",
			"cinematiccataclysm:maledictus_cutscene",
			"cinematiccataclysm:scylla_cutscene",
			"create:carriage_contraption",
			"create:contraption",
			"create:gantry_contraption",
			"create:stationary_contraption",
			"createbigcannons:cannon_carriage",
			"createbigcannons:pitch_contraption",
			"drg_flares:drg_flare",
			"drg_flares:drg_flares",
			"minecraft:acacia_boat",
			"minecraft:acacia_chest_boat",
			"minecraft:bamboo_chest_raft",
			"minecraft:bamboo_raft",
			"minecraft:birch_boat",
			"minecraft:birch_chest_boat",
			"minecraft:block_display",
			"minecraft:boat",
			"minecraft:cherry_boat",
			"minecraft:cherry_chest_boat",
			"minecraft:dark_oak_boat",
			"minecraft:dark_oak_chest_boat",
			"minecraft:firework_rocket",
			"minecraft:item_display",
			"minecraft:jungle_boat",
			"minecraft:jungle_chest_boat",
			"minecraft:mangrove_boat",
			"minecraft:mangrove_chest_boat",
			"minecraft:oak_boat",
			"minecraft:oak_chest_boat",
			"minecraft:pale_oak_boat",
			"minecraft:pale_oak_chest_boat",
			"minecraft:spruce_boat",
			"minecraft:spruce_chest_boat",
			"minecraft:text_display",
			"mts:builder_existing",
			"mts:builder_rendering",
			"mts:builder_seat",
			"voidscape:corrupted_pawn"
	));

	public static final Set<BlockEntityType<?>> BLOCK_ENTITY_WHITELIST = new HashSet<>();
	public static final Set<EntityType<?>> ENTITY_WHITELIST = new HashSet<>();
	public static final Set<EntityType<?>> TICK_CULLING_WHITELIST = new HashSet<>();
	private static boolean whitelistsInitialized = false;

	public static int culledEntities = 0;
	public static int lastCulledEntities = 0;
	public static int actualCulledEntitiesCurrentFrame = 0;
	public static int lastActualCulledEntities = 0;

	public static int renderedBlockEntities = 0;
	public static int skippedBlockEntities = 0;
	public static int consideredBlockEntities = 0;

	public static int tickedEntities = 0;
	public static int skippedEntityTicks = 0;

	private static final CullTask cullTask;
	private static final Thread cullThread;

	static {
		CullingDataProvider provider = new CullingDataProvider();
		OcclusionCullingInstance culling = new OcclusionCullingInstance(128, provider);
		cullTask = new CullTask(culling);
		cullThread = new Thread(cullTask, "Lucent-CullThread");
		cullThread.setDaemon(true);
		cullThread.setUncaughtExceptionHandler((thread, ex) -> ex.printStackTrace());
		cullThread.start();
	}

	{
		WorldRenderEvents.START_MAIN.register(context -> {
			lastCulledEntities = culledEntities;
			lastActualCulledEntities = actualCulledEntitiesCurrentFrame;
			actualCulledEntitiesCurrentFrame = 0;
			renderedBlockEntities = 0;
			skippedBlockEntities = 0;

			if (!whitelistsInitialized) {
				initWhitelists();
			}

			if (isEnabled() && mc.level != null && mc.player != null) {
				Vec3 camPos = UWorld.getCameraPos();
				cullTask.tryPopulateAndSwap(mc.level.entitiesForRendering(), camPos);
			}
		});
	}

	public static void initWhitelists() {
		whitelistsInitialized = true;
		for (String id : BLOCK_ENTITY_WHITELIST_STRINGS) {
			Identifier loc = Identifier.tryParse(id);
			if (loc != null) {
				BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(loc).ifPresent(BLOCK_ENTITY_WHITELIST::add);
			}
		}
		for (String id : ENTITY_WHITELIST_STRINGS) {
			Identifier loc = Identifier.tryParse(id);
			if (loc != null) {
				BuiltInRegistries.ENTITY_TYPE.getOptional(loc).ifPresent(ENTITY_WHITELIST::add);
			}
		}
		for (String id : TICK_CULLING_WHITELIST_STRINGS) {
			Identifier loc = Identifier.tryParse(id);
			if (loc != null) {
				BuiltInRegistries.ENTITY_TYPE.getOptional(loc).ifPresent(TICK_CULLING_WHITELIST::add);
			}
		}
	}

	public static void addDynamicEntityWhitelist(Predicate<Entity> filter) {
		IGNORE_FILTERS.add(filter);
	}

	public static void addDynamicBlockEntityWhitelist(Predicate<BlockEntity> filter) {
		BLOCK_ENTITY_IGNORE_FILTERS.add(filter);
	}

	public static boolean shouldCheckEntity(Entity entity) {
		if (ENTITY_WHITELIST.contains(entity.getType())) {
			return false;
		}

		boolean shouldCull;
		if (entity instanceof Player) {
			shouldCull = CullPlayers && !LucentClientUtils.checkInDungeon();
		} else if (entity instanceof ItemEntity) {
			shouldCull = CullDroppedItems;
		} else {
			shouldCull = CullEntities;
		}

		if (shouldCull) {
			for (Predicate<Entity> filter : IGNORE_FILTERS) {
				if (filter.test(entity)) {
					return false;
				}
			}
		}
		return shouldCull;
	}

	public static boolean isBlockEntityWhitelisted(BlockEntity entity) {
		if (BLOCK_ENTITY_WHITELIST.contains(entity.getType())) {
			return true;
		}
		for (Predicate<BlockEntity> filter : BLOCK_ENTITY_IGNORE_FILTERS) {
			if (filter.test(entity)) {
				return true;
			}
		}
		return false;
	}

	public static String getCulledEntitiesInfo() {
		return "Culled: " + lastActualCulledEntities + " (Frustum) / " + lastCulledEntities + " (Total)";
	}

}