package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import silence.simsool.lucent.general.utils.useful.UScreen;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

	@Inject(method = "baseTick", at = @At("HEAD"))
	private void onUpdateSwingTime(CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self instanceof LocalPlayer) {
			AnimationsMod.onUpdateSwingTime();
		}
	}

	@Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/component/SwingAnimation;Z)Z", at = @At("HEAD"), cancellable = true)
	private void onSwing(CallbackInfoReturnable<Boolean> cir) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self instanceof LocalPlayer) {
			if (AnimationsMod.isEnabled() && AnimationsMod.FixSlotDrop && UScreen.isScreenOpen()) {
				cir.setReturnValue(false);
				return;
			}
			AnimationsMod.onSwing();
		}
	}

	@Inject(method = "getEffectBlendFactor", at = @At("HEAD"), cancellable = true)
	private void onGetEffectBlendFactor(Holder<MobEffect> effect, float partialTick, CallbackInfoReturnable<Float> cir) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self instanceof LocalPlayer) {
			if (AnimationsMod.isEnabled() && AnimationsMod.HideNausea && effect.equals(MobEffects.NAUSEA)) {
				cir.setReturnValue(0.0f);
			}
		}
	}
}