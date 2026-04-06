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
	private double currentZoom = 1.0;

	@Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
	private void onGetFov(Camera camera, float f, boolean bl, CallbackInfoReturnable<Float> cir) {

		if (ZoomMod.isEnabled()) {
			double targetZoom = ZoomMod.ZoomKey.isKeyDown() ? (double) ZoomMod.ZoomFactor : 1.0;

			if (ZoomMod.SmoothZoom) currentZoom = Mth.lerp(f * 0.2f, currentZoom, targetZoom);
			else currentZoom = targetZoom;

			if (currentZoom != 1.0) {
				// 리턴 값이 Float이므로 계산 후 다시 float로 형변환해서 입력
				float originalFov = cir.getReturnValue();
				cir.setReturnValue((float) (originalFov / currentZoom));
			}
		}
	}

}