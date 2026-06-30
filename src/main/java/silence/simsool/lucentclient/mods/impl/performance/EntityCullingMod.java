package silence.simsool.lucentclient.mods.impl.performance;

import static silence.simsool.lucent.Lucent.mc;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
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
		name = "lucent.config.lucentclient.entitycullingmod.property.showdebuginfo.name",
		description = "lucent.config.lucentclient.entitycullingmod.property.showdebuginfo.description",
		category = "Debug"
	)
	public static boolean ShowDebugInfo = true;

	public static final List<Predicate<Entity>> IGNORE_FILTERS = new CopyOnWriteArrayList<>();
	public static final Map<Entity, VisibilityState> visibilityCache = new WeakHashMap<>();
	public static int culledEntities = 0;
	public static int lastCulledEntities = 0;
	public static final double FAR_DIST_SQ = 1024.0;

	public static class VisibilityState {
		public boolean visible = true;
		public long lastCheckTick = 0;
	}

	{
		WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
			lastCulledEntities = culledEntities;
			culledEntities = 0;
		});
	}

	public static String getCulledEntitiesInfo() {
		return "Culled Entities: " + lastCulledEntities;
	}

	public static boolean isVisibleOptimized(Vec3 camPos, AABB box, Entity cameraEntity, double distSq) {
		double cx = (box.minX + box.maxX) * 0.5;
		double cy = (box.minY + box.maxY) * 0.5;
		double cz = (box.minZ + box.maxZ) * 0.5;

		// 1. 가장 확률이 높은 중심점 검사 (보이면 즉시 탈출)
		if (fastClip(camPos, new Vec3(cx, cy, cz), cameraEntity)) return true;

		double top = box.maxY - 0.05;

		// 2. 32블럭 이상 먼 거리는 중심과 상단만 검사 (극한의 최적화)
		if (distSq > FAR_DIST_SQ) {
			return fastClip(camPos, new Vec3(cx, top, cz), cameraEntity);
		}

		// 3. 근/중거리 엔티티는 상/하/좌/우 4개 추가 검사 (총 5포인트)
		double bot = box.minY + 0.05;
		double ex = (box.maxX - box.minX) * 0.35;
		double ez = (box.maxZ - box.minZ) * 0.35;

		Vec3[] pts = {
			new Vec3(cx, top, cz),           // 상
			new Vec3(cx, bot, cz),           // 하
			new Vec3(cx - ex, cy, cz - ez),  // 좌 대각
			new Vec3(cx + ex, cy, cz + ez)   // 우 대각
		};

		for (Vec3 end : pts) {
			if (fastClip(camPos, end, cameraEntity)) return true;
		}
		return false;
	}

	private static boolean fastClip(Vec3 start, Vec3 end, Entity cameraEntity) {
		ClipContext ctx = new ClipContext(start, end, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, cameraEntity);
		return mc.level.clip(ctx).getType() == HitResult.Type.MISS;
	}

}