package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;

@Mixin(LogoRenderer.class)
public abstract class MixinLogoRenderer {

	@Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IF)V", at = @At("HEAD"), cancellable = true, require = 0)
	private void onRender3(GuiGraphics graphics, int width, float alpha, CallbackInfo ci) {
		ci.cancel();
	}

	@Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At("HEAD"), cancellable = true, require = 0)
	private void onRender4(GuiGraphics graphics, int width, float alpha, int height, CallbackInfo ci) {
		ci.cancel();
	}

}