package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import silence.simsool.lucentclient.mixin.accessors.RenderTypeFeatureRendererAccessor;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(targets = "net.minecraft.client.renderer.feature.TextFeatureRenderer$GlyphRenderer")
public abstract class MixinTextFeatureRenderer_GlyphRenderer {

	@Unique
	private RenderType lucent$lastRenderType;

	@Unique
	private VertexConsumer lucent$lastVertexConsumer;

	@Redirect(method = "acceptRenderable", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/TextFeatureRenderer;getVertexBuilder(Lnet/minecraft/client/renderer/rendertype/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
	private VertexConsumer reduceGetVertexBuilderCalls(final TextFeatureRenderer instance, final RenderType renderType) {
		if (!FastRenderMod.isEnabled() || !FastRenderMod.FastTextLookup) {
			return ((RenderTypeFeatureRendererAccessor) instance).invokeGetVertexBuilder(renderType);
		}
		if (this.lucent$lastRenderType != renderType) {
			this.lucent$lastRenderType = renderType;
			this.lucent$lastVertexConsumer = ((RenderTypeFeatureRendererAccessor) instance).invokeGetVertexBuilder(renderType);
		}
		return this.lucent$lastVertexConsumer;
	}

}