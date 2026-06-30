package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MouseHandler;
import silence.simsool.lucentclient.mods.impl.utility.ZoomMod;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {

	@Shadow
	private double accumulatedDX;
	@Shadow
	private double accumulatedDY;

	@Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
	private void onScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
		if (ZoomMod.handleScroll(yoffset)) {
			ci.cancel();
		}
	}

	@Inject(method = "turnPlayer", at = @At("HEAD"))
	private void onTurnPlayer(CallbackInfo ci) {
		if (ZoomMod.isEnabled() && ZoomMod.isZoomKeyDown()) {
			this.accumulatedDX *= ZoomMod.ZoomSensitivity;
			this.accumulatedDY *= ZoomMod.ZoomSensitivity;
		}
	}

}