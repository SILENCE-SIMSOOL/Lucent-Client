package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import java.util.SequencedMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import silence.simsool.lucentclient.features.fastrender.BatchableBufferSource;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(RenderBuffers.class)
public abstract class MixinRenderBuffers {

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;immediateWithBuffers(Ljava/util/SequencedMap;Lcom/mojang/blaze3d/vertex/ByteBufferBuilder;)Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;", ordinal = 0))
	private MultiBufferSource.BufferSource replaceEntityBufferSource(SequencedMap<RenderType, ByteBufferBuilder> fixedBuffers, ByteBufferBuilder sharedBuffer) {
		if (FastRenderMod.isEnabled() && FastRenderMod.EnhancedBatching) {
			return new BatchableBufferSource(sharedBuffer, fixedBuffers);
		}
		return MultiBufferSource.immediateWithBuffers(fixedBuffers, sharedBuffer);
	}

}