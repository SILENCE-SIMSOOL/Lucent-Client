package silence.simsool.lucentclient.mods.impl.performance;

import static silence.simsool.lucent.Lucent.mc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
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
		name = "lucent.config.lucentclient.entitycullingmod.property.rendernametagsthroughwalls.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.rendernametagsthroughwalls.description"
	)
	public static boolean RenderNametagsThroughWalls = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.entitycullingmod.property.showdebuginfo.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.showdebuginfo.description",
		category = "Debug"
	)
	public static boolean ShowDebugInfo = true;

	public static final List<Predicate<Entity>> IGNORE_FILTERS = new CopyOnWriteArrayList<>();
	public static int culledEntities = 0;
	public static int lastCulledEntities = 0;
	public static int actualCulledEntitiesCurrentFrame = 0;
	public static int lastActualCulledEntities = 0;

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
		LevelRenderEvents.START_MAIN.register(context -> {
			lastCulledEntities = culledEntities;
			lastActualCulledEntities = actualCulledEntitiesCurrentFrame;
			actualCulledEntitiesCurrentFrame = 0;
			if (isEnabled() && mc.level != null && mc.player != null) {
				Vec3 camPos = UWorld.getCameraPos();
				cullTask.tryPopulateAndSwap(mc.level.entitiesForRendering(), camPos);
			}
		});
	}

	public static boolean shouldCheckEntity(Entity entity) {
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

	public static String getCulledEntitiesInfo() {
		return "Culled: " + lastActualCulledEntities + " (Frustum) / " + lastCulledEntities + " (Total)";
	}

}