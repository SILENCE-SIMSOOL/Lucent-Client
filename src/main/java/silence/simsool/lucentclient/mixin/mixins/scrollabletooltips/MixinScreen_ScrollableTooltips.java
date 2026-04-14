package silence.simsool.lucentclient.mixin.mixins.scrollabletooltips;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screens.Screen;
import silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips.utils.ScrollTracker;

@Mixin(Screen.class)
public abstract class MixinScreen_ScrollableTooltips {

	@Inject(method = "onClose", at = @At("HEAD"))
	public void resetTrackerOnScreenClose(CallbackInfo info) {
		ScrollTracker.reset();
	}

}