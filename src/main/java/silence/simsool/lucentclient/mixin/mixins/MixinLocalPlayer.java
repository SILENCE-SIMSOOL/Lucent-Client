package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import silence.simsool.lucent.general.utils.useful.UScreen;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;
import silence.simsool.lucentclient.mods.impl.utility.AlwaysSprintMod;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {

	@Inject(method = "swing(Lnet/minecraft/world/InteractionHand;)V", at = @At("HEAD"), cancellable = true)
	private void cancelGuiDropSwing(InteractionHand hand, CallbackInfo ci) {
		if (AnimationsMod.FixSlotDrop && UScreen.isScreenOpen()) {
			ci.cancel();
		}
	}

	@Inject(method = "aiStep", at = @At("HEAD"))
	private void onAiStep(CallbackInfo ci) {
		if (AlwaysSprintMod.isEnabled()) {
			LocalPlayer self = (LocalPlayer) (Object) this;
			if (self.input.hasForwardImpulse() && !self.isSprinting() && !self.isShiftKeyDown() && !self.hasEffect(MobEffects.BLINDNESS) && self.getFoodData().getFoodLevel() > 6) {
				self.setSprinting(true);
			}
		}
	}

}