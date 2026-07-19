package silence.simsool.lucentclient.mixin.mixins;

import static silence.simsool.lucent.Lucent.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import silence.simsool.lucentclient.mods.impl.graphics.NametagsMod;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer {

	@Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At("HEAD"), cancellable = true)
	private void viewOwnLabel(LivingEntity entity, double distanceSq, CallbackInfoReturnable<Boolean> cir) {
		if (NametagsMod.CustomNametags && NametagsMod.ShowSelfNametag) {
			if (mc.player != null && entity == mc.player) {
				boolean isThirdPerson = !mc.options.getCameraType().isFirstPerson();
				boolean isVisibleToPlayer = !entity.isInvisibleTo(mc.player);
				boolean isHudEnabled = !mc.options.hideGui;
				if (isThirdPerson && isHudEnabled && isVisibleToPlayer && !entity.isVehicle()) {
					cir.setReturnValue(true);
				}
			}
		}
	}

}