package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

	@Inject(method = "updateSwingTime", at = @At("HEAD"))
	private void onUpdateSwingTime(CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self instanceof LocalPlayer) {
			AnimationsMod.onUpdateSwingTime();
		}
	}

	@Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"))
	private void onSwing(CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self instanceof LocalPlayer) {
			AnimationsMod.onSwing();
		}
	}

}