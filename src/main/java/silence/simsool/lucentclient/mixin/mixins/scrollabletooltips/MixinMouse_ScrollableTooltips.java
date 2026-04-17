package silence.simsool.lucentclient.mixin.mixins.scrollabletooltips;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.MouseHandler;
import silence.simsool.lucent.general.utils.useful.UDisplay;
import silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips.ScrollableTooltipsMod;
import silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips.utils.ScrollTracker;

@Mixin(MouseHandler.class)
public class MixinMouse_ScrollableTooltips {

	@Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
	private void trackWheel(long window, double horizontal, double vertical, CallbackInfo info) {
		if (ScrollableTooltipsMod.isEnabled() && !ScrollTracker.isLocked()) {
			Window mcWindow = UDisplay.getWindow();
			boolean isHorizontal = ScrollableTooltipsMod.horizontal.isKeyDown() || (ScrollableTooltipsMod.UseLShift && InputConstants.isKeyDown(mcWindow, GLFW.GLFW_KEY_LEFT_SHIFT));

			if (isHorizontal) scrollX(vertical);
			else scrollY(vertical);

			if (horizontal > 0) ScrollTracker.scrollLeft();
			else if (horizontal < 0) ScrollTracker.scrollRight();

			info.cancel();
		}
	}

	@Unique
	private void scrollX(double vertical) {
		if (ScrollableTooltipsMod.InvertXScroll) {
			if (vertical > 0) ScrollTracker.scrollRight();
			else if (vertical < 0) ScrollTracker.scrollLeft();
		}
		else {
			if (vertical > 0) ScrollTracker.scrollLeft();
			else if (vertical < 0) ScrollTracker.scrollRight();
		}
	}

	@Unique
	private void scrollY(double vertical) {
		if (ScrollableTooltipsMod.InvertYScroll) {
			if (vertical > 0) ScrollTracker.scrollDown();
			else if (vertical < 0) ScrollTracker.scrollUp();
		}
		else {
			if (vertical > 0) ScrollTracker.scrollUp();
			else if (vertical < 0) ScrollTracker.scrollDown();
		}
	}

}