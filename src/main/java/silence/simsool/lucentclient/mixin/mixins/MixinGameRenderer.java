package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import silence.simsool.lucentclient.mods.impl.utility.ZoomMod;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

	@Unique
	private float currentZoom = 1.0f;

	@Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
	private void onGetFov(Camera camera, float f, boolean bl, CallbackInfoReturnable<Float> cir) {

		if (ZoomMod.isEnabled()) {
			float targetZoom = ZoomMod.getTargetZoom();

			if (ZoomMod.SmoothZoom) currentZoom = Mth.lerp(f * 0.2f, currentZoom, targetZoom);
			else currentZoom = targetZoom;

			if (currentZoom != 1.0) cir.setReturnValue(cir.getReturnValue() / currentZoom);
		}
	}

}