package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import silence.simsool.lucentclient.mods.impl.graphics.TimeChangerMod;

@Mixin(Level.class)
public class MixinLevel {

	@Inject(method = "getOverworldClockTime", at = @At("HEAD"), cancellable = true)
	private void onGetOverworldClockTime(CallbackInfoReturnable<Long> cir) {
		if ((Object) this instanceof ClientLevel) {
			if (TimeChangerMod.isEnabled()) {
				if (!TimeChangerMod.TimeSelection.equals("Off")) {
					float customTime = 0;
					switch (TimeChangerMod.TimeSelection) {
						case "Day": customTime = 0.0f; break;
						case "Noon": customTime = 0.25f; break; 
						case "Sunset": customTime = 0.5f; break;
						case "Night": customTime = 0.75f; break;
						case "Midnight": customTime = 0.85f; break;
						case "Custom": customTime = ((float) TimeChangerMod.CustomTimeValue) / 24000.0f; break;
					}
					cir.setReturnValue((long) (customTime * 24000.0f));
				}
			}
		}
	}

	@Inject(method = "getDefaultClockTime", at = @At("HEAD"), cancellable = true)
	private void onGetDefaultClockTime(CallbackInfoReturnable<Long> cir) {
		if ((Object) this instanceof ClientLevel) {
			if (TimeChangerMod.isEnabled()) {
				if (!TimeChangerMod.TimeSelection.equals("Off")) {
					float customTime = 0;
					switch (TimeChangerMod.TimeSelection) {
						case "Day": customTime = 0.0f; break;
						case "Noon": customTime = 0.25f; break; 
						case "Sunset": customTime = 0.5f; break;
						case "Night": customTime = 0.75f; break;
						case "Midnight": customTime = 0.85f; break;
						case "Custom": customTime = ((float) TimeChangerMod.CustomTimeValue) / 24000.0f; break;
					}
					cir.setReturnValue((long) (customTime * 24000.0f));
				}
			}
		}
	}

}