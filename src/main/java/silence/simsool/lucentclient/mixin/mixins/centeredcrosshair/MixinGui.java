package silence.simsool.lucentclient.mixin.mixins.centeredcrosshair;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import silence.simsool.lucentclient.ducks.DrawContextFloatDrawTexture;

@Mixin(Gui.class)
public class MixinGui {

	@WrapOperation(
		method = "renderCrosshair",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
			ordinal = 0
		)
	)
	private void centerTheCrosshair(GuiGraphics instance, RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, Operation<Void> original) {
		float scaleFactor = (float) Minecraft.getInstance().getWindow().getGuiScale();
		float scaledCenterX = (Minecraft.getInstance().getWindow().getWidth() / scaleFactor) / 2f;
		float scaledCenterY = (Minecraft.getInstance().getWindow().getHeight() / scaleFactor) / 2f;
		((DrawContextFloatDrawTexture) instance).centered_crosshair$blitSprite(
			renderPipeline,
			location,
			Math.round((scaledCenterX - 7.5f) * 4) / 4f,
			Math.round((scaledCenterY - 7.5f) * 4) / 4f,
			15,
			15
		);
	}
}