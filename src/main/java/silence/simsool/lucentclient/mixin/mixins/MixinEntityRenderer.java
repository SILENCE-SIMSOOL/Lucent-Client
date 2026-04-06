package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import silence.simsool.lucentclient.mods.impl.graphics.DeathAnimationMod;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity> {

	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private void onShouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		if (DeathAnimationMod.HideEntityDeathAnimation) {
			if (entity instanceof LivingEntity livingEntity && livingEntity.isDeadOrDying()) {
				cir.setReturnValue(false);
				return;
			}
		}
		if (DeathAnimationMod.HideArmorStandDeathAnimation) {
			if (entity instanceof ArmorStand armorStand) {
				Entity before = armorStand.level().getEntity(armorStand.getId() - 1);
				if (before instanceof LivingEntity livingEntity && livingEntity.isDeadOrDying()) {
					cir.setReturnValue(false);
					return;
				}
			}
		}
	}

}