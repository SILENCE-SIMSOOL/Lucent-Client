package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import silence.simsool.lucentclient.mods.impl.graphics.LoadingScreenMod;

@Mixin(LevelLoadingScreen.class)
public class MixinLevelLoadingScreen {

	@Shadow
	private LevelLoadTracker loadTracker;

	@Inject(method = { "extractRenderState", "extractBackground" }, at = @At("HEAD"), cancellable = true, require = 2)
	private void hideWorldLoadingScreen(CallbackInfo ci) {
		if (LoadingScreenMod.isEnabled() && LoadingScreenMod.HideWorldLoadingScreen && this.loadTracker.statusView() == null) ci.cancel();
	}

}