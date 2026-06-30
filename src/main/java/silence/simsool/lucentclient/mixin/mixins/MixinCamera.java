package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Camera;
import silence.simsool.lucent.general.utils.useful.UWorld;
import silence.simsool.lucent.ui.utils.UAnimation;
import silence.simsool.lucentclient.mods.impl.utility.ZoomMod;

@Mixin(Camera.class)
public class MixinCamera {

	@Unique
	private float currentZoom = 1.0f;

	@Inject(method = "calculateFov", at = @At("RETURN"), cancellable = true, remap = false)
	private void onCalculateFov(float partialTicks, CallbackInfoReturnable<Float> cir) {
		if (ZoomMod.isEnabled()) {
			float targetZoom = ZoomMod.getTargetZoom();

			if (ZoomMod.SmoothZoom) currentZoom = UAnimation.lerp(currentZoom, targetZoom, UWorld.getPartialTick());
			else currentZoom = targetZoom;

			if (currentZoom != 1.0f) cir.setReturnValue(cir.getReturnValue() / currentZoom);
		}
	}

}