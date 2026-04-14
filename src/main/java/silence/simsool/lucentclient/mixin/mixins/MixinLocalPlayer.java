package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import silence.simsool.lucentclient.mods.impl.utility.AlwaysSprintMod;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {

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