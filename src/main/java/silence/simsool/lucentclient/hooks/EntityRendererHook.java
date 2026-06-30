package silence.simsool.lucentclient.hooks;

import static silence.simsool.lucent.Lucent.mc;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.function.Predicate;

import silence.simsool.lucent.general.utils.useful.UWorld;
import silence.simsool.lucentclient.mods.impl.graphics.DeathAnimationMod;
import silence.simsool.lucentclient.mods.impl.graphics.HideFallingBlockMod;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class EntityRendererHook {

	public static void onShouldRender(Entity entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {

		if (HideFallingBlockMod.isEnabled()) {
			if (entity instanceof FallingBlockEntity) {
				cir.setReturnValue(false);
				return;
			}
		}

		if (DeathAnimationMod.isEnabled()) {
			if (entity instanceof LivingEntity livingEntity && livingEntity.isDeadOrDying()) {
				if (DeathAnimationMod.HideEntityDeathAnimation) {
					cir.setReturnValue(false);
					return;
				}
			}
			else if (entity instanceof ArmorStand armorStand) {
				Entity before = armorStand.level().getEntity(armorStand.getId() - 1);
				if (before instanceof LivingEntity livingEntity && livingEntity.isDeadOrDying()) {
					if (DeathAnimationMod.HideArmorStandDeathAnimation) {
						cir.setReturnValue(false);
						return;
					}
				}
			}
		}

		if (EntityCullingMod.isEnabled()) {
 
			boolean shouldCull;
			if (entity instanceof Player) {
				shouldCull = EntityCullingMod.CullPlayers && !LucentClientUtils.checkInDungeon();
			} else if (entity instanceof ItemEntity) {
				shouldCull = EntityCullingMod.CullDroppedItems;
			} else {
				shouldCull = EntityCullingMod.CullEntities;
			}

			if (shouldCull) {
				for (Predicate<Entity> filter : EntityCullingMod.IGNORE_FILTERS) {
					if (filter.test(entity)) {
						shouldCull = false;
						break;
					}
				}
			}

			if (shouldCull) {
				Entity cameraEntity = mc.getCameraEntity();
				if (entity == cameraEntity || mc.level == null || cameraEntity == null) return;

				if (!frustum.isVisible(entity.getBoundingBox())) {
					EntityCullingMod.culledEntities++;
					cir.setReturnValue(false);
					return;
				}

				double distSq = entity.distanceToSqr(cameraEntity);
				if (distSq < 64) return; // 바닐라 로직 타게 둠

				long currentTick = mc.level.getGameTime();
				EntityCullingMod.VisibilityState state = EntityCullingMod.visibilityCache.computeIfAbsent(entity, e -> new EntityCullingMod.VisibilityState());
		
				boolean needsUpdate = (currentTick - state.lastCheckTick) >= 5;

				if (!needsUpdate || (entity.getId() + currentTick) % 5 != 0) {
					if (state.lastCheckTick != 0 && !state.visible) {
						EntityCullingMod.culledEntities++;
						cir.setReturnValue(false);
						return;
					}
					else return;
				}

				Vec3 camPos = UWorld.getCameraPos();
				AABB box = entity.getBoundingBox().inflate(0.05);

				state.visible = EntityCullingMod.isVisibleOptimized(camPos, box, cameraEntity, distSq);
				state.lastCheckTick = currentTick;

				if (!state.visible) {
					EntityCullingMod.culledEntities++;
					cir.setReturnValue(false);
					return;
				}
			}
		}

	}

}