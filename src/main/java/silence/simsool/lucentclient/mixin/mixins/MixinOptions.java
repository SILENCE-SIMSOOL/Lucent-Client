package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Options;
import net.minecraft.client.CameraType;
import silence.simsool.lucentclient.mods.impl.utility.BetterF5Mod;

@Mixin(Options.class)
public class MixinOptions {

	@Inject(method = "setCameraType(Lnet/minecraft/client/CameraType;)V", at = @At("HEAD"), cancellable = true)
	private void onSetCameraType(CameraType cameraType, CallbackInfo ci) {
		if (BetterF5Mod.isEnabled() && cameraType == CameraType.THIRD_PERSON_FRONT) {
			((Options) (Object) this).setCameraType(CameraType.FIRST_PERSON);
			ci.cancel();
		}
	}

}
