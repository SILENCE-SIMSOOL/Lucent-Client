package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(targets = "net.minecraft.client.gui.Font$GlyphVisitor$1")
public abstract class MixinFont_GlyphVisitor {

	@Unique
	private RenderType lucent$lastRenderType;

	@Unique
	private VertexConsumer lucent$lastVertexConsumer;

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/rendertype/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
	private VertexConsumer reduceGetBufferCalls(final MultiBufferSource instance, final RenderType renderType) {
		if (!FastRenderMod.isEnabled() || !FastRenderMod.FastTextLookup) {
			return instance.getBuffer(renderType);
		}
		if (this.lucent$lastRenderType != renderType) {
			this.lucent$lastRenderType = renderType;
			this.lucent$lastVertexConsumer = instance.getBuffer(renderType);
		}
		return this.lucent$lastVertexConsumer;
	}

}