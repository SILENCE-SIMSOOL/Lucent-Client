package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

	@Shadow
	public abstract boolean hasEffect(Holder<MobEffect> mobEffect);

	@Shadow
	public abstract MobEffectInstance getEffect(Holder<MobEffect> mobEffect);

	@Inject(method = "getCurrentSwingDuration", at = @At("HEAD"), cancellable = true)
	private void onGetCurrentSwingDuration(CallbackInfoReturnable<Integer> cir) {
		if (AnimationsMod.isEnabled()) {
			LivingEntity self = (LivingEntity) (Object) this;

			int duration = 6 - (AnimationsMod.SwingSpeed - 8);

			if (!AnimationsMod.IgnoreHaste && self.hasEffect(MobEffects.HASTE)) {
				MobEffectInstance effect = self.getEffect(MobEffects.HASTE);
				if (effect != null) {
					duration -= (1 + effect.getAmplifier());
				}
			}

			if (!AnimationsMod.IgnoreHaste && self.hasEffect(MobEffects.MINING_FATIGUE)) {
				MobEffectInstance effect = self.getEffect(MobEffects.MINING_FATIGUE);
				if (effect != null) {
					duration += (1 + effect.getAmplifier()) * 2;
				}
			}

			if (duration < 2) duration = 2;
			else if (duration > 20) duration = 20;

			cir.setReturnValue(duration);
		}
	}

}