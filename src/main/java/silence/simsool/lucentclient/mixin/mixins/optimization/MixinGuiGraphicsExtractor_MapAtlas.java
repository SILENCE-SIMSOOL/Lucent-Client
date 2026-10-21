package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.MapRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import silence.simsool.lucentclient.features.fastrender.IMapRenderState;
import silence.simsool.lucentclient.features.fastrender.MapAtlas;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(GuiGraphicsExtractor.class)
public abstract class MixinGuiGraphicsExtractor_MapAtlas {

	@WrapOperation(method = "map", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;innerBlit(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lcom/mojang/renderpearl/api/textures/GpuTextureView;Lcom/mojang/renderpearl/api/textures/GpuSampler;IIIIFFFFI)V", ordinal = 0))
	private void modifyTextureCoordinates(final GuiGraphicsExtractor instance, final RenderPipeline pipeline, final GpuTextureView textureView, final GpuSampler sampler, final int x0, final int y0, final int x1, final int y1, float u0, float u1, float v0, float v1, final int color, final Operation<Void> original, @Local(name = "mapRenderState", argsOnly = true) final MapRenderState mapRenderState) {
		if (FastRenderMod.isEnabled() && FastRenderMod.MapAtlasGeneration) {
			final IMapRenderState immediatelyFast$mapRenderState = (IMapRenderState) mapRenderState;
			if (immediatelyFast$mapRenderState.immediatelyFast$getAtlasTextureId() != null && immediatelyFast$mapRenderState.immediatelyFast$getAtlasTextureId().equals(mapRenderState.texture)) {
				u0 = (float) immediatelyFast$mapRenderState.immediatelyFast$getAtlasX() / MapAtlas.ATLAS_SIZE;
				u1 = (float) (immediatelyFast$mapRenderState.immediatelyFast$getAtlasX() + MapAtlas.MAP_SIZE) / MapAtlas.ATLAS_SIZE;
				v0 = (float) immediatelyFast$mapRenderState.immediatelyFast$getAtlasY() / MapAtlas.ATLAS_SIZE;
				v1 = (float) (immediatelyFast$mapRenderState.immediatelyFast$getAtlasY() + MapAtlas.MAP_SIZE) / MapAtlas.ATLAS_SIZE;
			}
		}
		original.call(instance, pipeline, textureView, sampler, x0, y0, x1, y1, u0, u1, v0, v1, color);
	}

}