package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.backend.opengl.DirectStateAccess;
import com.mojang.renderpearl.backend.opengl.GlConst;
import com.mojang.renderpearl.backend.opengl.GlStateManager;
import java.nio.ByteBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(targets = "com.mojang.renderpearl.backend.opengl.GlCommandEncoder")
public abstract class MixinGlCommandEncoder {

	@Redirect(method = "submitRenderPass", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/backend/opengl/GlStateManager;_glBindFramebuffer(II)V"))
	private void dontUnbindFramebuffer(final int target, final int framebuffer) {
		if (!FastRenderMod.isEnabled() || !FastRenderMod.AvoidFramebufferSwitching) {
			GlStateManager._glBindFramebuffer(target, framebuffer);
		}
	}

	@Inject(method = "presentTexture", at = @At("HEAD"))
	private void unbindFramebufferBeforePresenting(final CallbackInfo ci) {
		if (FastRenderMod.isEnabled() && FastRenderMod.AvoidFramebufferSwitching) {
			GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
		}
	}

	@Redirect(method = "writeToBuffer", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/backend/opengl/DirectStateAccess;bufferSubData(IJLjava/nio/ByteBuffer;I)V"))
	private void fixSlowBufferUploadOnAppleGpu(final DirectStateAccess instance, final int buffer, final long offset, final ByteBuffer data, final int usage, @Local(name = "slice", argsOnly = true) final GpuBufferSlice slice) {
		if (FastRenderMod.isEnabled() && FastRenderMod.FixSlowBufferUploadOnAppleGpu && offset == 0 && slice.length() == slice.buffer().size()) {
			instance.bufferData(buffer, data, usage);
		} else {
			instance.bufferSubData(buffer, offset, data, usage);
		}
	}

}