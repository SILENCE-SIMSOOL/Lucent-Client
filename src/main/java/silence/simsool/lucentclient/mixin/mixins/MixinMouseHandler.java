package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import silence.simsool.lucentclient.mods.impl.hud.CPSMod;
import silence.simsool.lucentclient.mods.impl.utility.ZoomMod;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {

	@Inject(method = "onButton", at = @At("HEAD"))
	private void onMouseClick(long handle, MouseButtonInfo buttonInfo, int action, CallbackInfo ci) {
		if (action == 1) {
			if (buttonInfo.button() == 0) {
				CPSMod.addLeftClick();
			}
		}
	}

	@Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
	private void onScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
		if (ZoomMod.handleScroll(yoffset)) {
			ci.cancel();
		}
	}

}