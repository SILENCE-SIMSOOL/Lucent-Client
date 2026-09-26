package silence.simsool.lucentclient.mods.impl.performance.culling;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import silence.simsool.lucentclient.mixin.accessors.EntityRendererAccessor;

public class CullingHelper {

	@SuppressWarnings("unchecked")
	public static AABB setupAABB(BlockEntity entity, BlockPos pos) {
		if (entity instanceof BannerBlockEntity) {
			return new AABB(pos).inflate(0, 1, 0);
		}
		var dispatcher = mc.getBlockEntityRenderDispatcher();
		if (dispatcher != null) {
			var renderer = dispatcher.getRenderer(entity);
			if (renderer instanceof BlockEntityRenderFabricExtension<?> extension) {
				return ((BlockEntityRenderFabricExtension<BlockEntity>) extension).getRenderBoundingBox(entity);
			}
		}
		return new AABB(pos);
	}

	public static AABB getCullingBox(Entity entity) {
		if (entity instanceof ArmorStand armorStand && armorStand.isMarker()) {
			return EntityTypes.ARMOR_STAND.getDimensions().makeBoundingBox(entity.position());
		}
		return entity.getBoundingBox();
	}

	public static boolean ignoresCulling(Entity entity) {
		if (mc.player != null && entity == mc.player) {
			return true;
		}
		Entity cameraEntity = mc.getCameraEntity();
		if (cameraEntity != null && entity == cameraEntity) {
			return true;
		}
		var dispatcher = mc.getEntityRenderDispatcher();
		if (dispatcher != null) {
			var renderer = dispatcher.getRenderer(entity);
			if (renderer instanceof EntityRendererAccessor accessor) {
				return !accessor.invokeAffectedByCulling(entity);
			}
		}
		return false;
	}

}