package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

@Mixin(PanoramaRenderer.class)
public class MixinPanoramaRenderer {

	private static final Identifier BACKGROUND_LOCATION = Identifier.parse("lucentclient:textures/background.png");

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onRender(GuiGraphics graphics, int width, int height, boolean shouldSpin, CallbackInfo ci) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_LOCATION, 0, 0, 0.0F, 0.0F, width, height, width, height, width, height);
		ci.cancel();
	}

}
