package silence.simsool.lucentclient.mixin.mixins.scrollabletooltips;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips.utils.ScrollTracker;

@Mixin(Gui.class)
public abstract class MixinScreen_ScrollableTooltips {

	@Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;removed()V", shift = At.Shift.AFTER))
	public void resetTrackerOnScreenClose(Screen nextScreen, CallbackInfo info) {
		ScrollTracker.reset();
	}

}