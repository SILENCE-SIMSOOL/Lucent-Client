package silence.simsool.lucentclient.hooks;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.FallingBlockEntity;
import silence.simsool.lucentclient.mods.impl.graphics.DeathAnimationMod;
import silence.simsool.lucentclient.mods.impl.graphics.HideFallingBlockMod;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;
import silence.simsool.lucentclient.mods.impl.performance.culling.Cullable;

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
			} else if (entity instanceof ArmorStand armorStand) {
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
			if (entity instanceof Cullable cullable && cullable.isCulled()) {
				if (EntityCullingMod.RenderNametagsThroughWalls && entity.shouldShowName()) {
					return;
				}
				cir.setReturnValue(false);
				if (frustum != null && frustum.isVisible(entity.getBoundingBox())) {
					EntityCullingMod.actualCulledEntitiesCurrentFrame++;
				}
				return;
			}
		}

	}

}