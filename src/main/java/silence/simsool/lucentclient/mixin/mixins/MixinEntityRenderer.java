package silence.simsool.lucentclient.mixin.mixins;

import static silence.simsool.lucent.Lucent.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import silence.simsool.lucentclient.mods.impl.graphics.DeathAnimationMod;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;
import silence.simsool.lucentclient.utils.LucentClientUtils;


@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity> {

	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private void onShouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		if (DeathAnimationMod.isEnabled()) {
			if (entity instanceof LivingEntity livingEntity && livingEntity.isDeadOrDying()) {
				if (DeathAnimationMod.HideEntityDeathAnimation) {
					cir.setReturnValue(false);
					return;
				}
			}
			if (entity instanceof ArmorStand armorStand) {
				Entity before = armorStand.level().getEntity(armorStand.getId() - 1);
				if (before instanceof LivingEntity livingEntity && livingEntity.isDeadOrDying()) {
					if (DeathAnimationMod.HideArmorStandDeathAnimation) {
						cir.setReturnValue(false);
						return;
					}
				}
			}
		}
		cir.setReturnValue(true);
	}

	@Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
	private void onShouldRenderReturn(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && EntityCullingMod.isEnabled()) {

			if (entity instanceof Player) {
				if (!EntityCullingMod.CullPlayers) return;
				if (LucentClientUtils.checkInDungeon()) return;
			}

			else {
				if (!EntityCullingMod.CullEntities) return;
			}

			Entity cameraEntity = mc.getCameraEntity();

			if (entity != cameraEntity && mc.level != null && cameraEntity != null) {
				Vec3 start = mc.gameRenderer.getMainCamera().position();
				AABB box = entity.getBoundingBox().inflate(0.1D); 

				// 8 corners + center
				Vec3[] points = new Vec3[] {
					new Vec3(box.minX, box.minY, box.minZ),
					new Vec3(box.minX, box.minY, box.maxZ),
					new Vec3(box.minX, box.maxY, box.minZ),
					new Vec3(box.minX, box.maxY, box.maxZ),
					new Vec3(box.maxX, box.minY, box.minZ),
					new Vec3(box.maxX, box.minY, box.maxZ),
					new Vec3(box.maxX, box.maxY, box.minZ),
					new Vec3(box.maxX, box.maxY, box.maxZ),
					box.getCenter()
				};

				boolean visible = false;
				for (Vec3 end : points) {
					ClipContext context = new ClipContext(
						start, end, 
						ClipContext.Block.COLLIDER, 
						ClipContext.Fluid.NONE, 
						cameraEntity
					);
					HitResult result = mc.level.clip(context);
					if (result.getType() == HitResult.Type.MISS) {
						visible = true;
						break;
					}
				}

				if (!visible) {
					EntityCullingMod.culledEntities++;
					cir.setReturnValue(false);
				}
			}
		}
	}

}