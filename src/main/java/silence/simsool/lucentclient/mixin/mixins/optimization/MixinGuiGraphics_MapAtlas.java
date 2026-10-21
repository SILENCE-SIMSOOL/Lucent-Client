package silence.simsool.lucentclient.mixin.mixins.optimization;

import static silence.simsool.lucentclient.features.fastrender.MapAtlasTexture.ATLAS_SIZE;
import static silence.simsool.lucentclient.features.fastrender.MapAtlasTexture.MAP_SIZE;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.state.MapRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import silence.simsool.lucentclient.features.fastrender.IMapRenderState;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics_MapAtlas {

	@WrapOperation(method = "submitMapRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;submitBlit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lcom/mojang/blaze3d/textures/GpuTextureView;Lcom/mojang/blaze3d/textures/GpuSampler;IIIIFFFFI)V", ordinal = 0))
	private void modifyTextureCoordinates(GuiGraphics instance, RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int x1, int y1, int x2, int y2, float u1, float u2, float v1, float v2, int color, Operation<Void> original, @Local(argsOnly = true) MapRenderState renderState) {
		if (FastRenderMod.isEnabled() && FastRenderMod.MapAtlasGeneration) {
			final IMapRenderState mapRenderState = (IMapRenderState) renderState;
			if (mapRenderState.lucent$getAtlasTexture() != null && mapRenderState.lucent$getAtlasTexture().getTextureId().equals(renderState.texture)) {
				u1 = (float) mapRenderState.lucent$getAtlasX() / ATLAS_SIZE;
				u2 = (float) (mapRenderState.lucent$getAtlasX() + MAP_SIZE) / ATLAS_SIZE;
				v1 = (float) mapRenderState.lucent$getAtlasY() / ATLAS_SIZE;
				v2 = (float) (mapRenderState.lucent$getAtlasY() + MAP_SIZE) / ATLAS_SIZE;
			}
		}
		original.call(instance, pipeline, textureView, sampler, x1, y1, x2, y2, u1, u2, v1, v2, color);
	}

}