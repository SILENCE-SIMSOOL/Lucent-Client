package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.DirectStateAccess;
import com.mojang.blaze3d.opengl.GlCommandEncoder;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import java.nio.ByteBuffer;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(GlCommandEncoder.class)
public abstract class MixinGlCommandEncoder {

	@Unique
	private static final boolean IS_APPLE_SILICON = Util.getPlatform() == Util.OS.OSX &&
			(System.getProperty("os.arch").equals("aarch64") || System.getProperty("os.arch").equals("arm64"));

	@WrapWithCondition(method = "finishRenderPass", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/opengl/GlStateManager;_glBindFramebuffer(II)V"))
	private boolean dontUnbindFramebuffer(int target, int framebuffer) {
		if (FastRenderMod.isEnabled() && FastRenderMod.AvoidRedundantFramebufferSwitching) {
			return false;
		}
		return true;
	}

	@Inject(method = "presentTexture", at = @At("HEAD"))
	private void unbindFramebufferBeforePresenting(CallbackInfo ci) {
		if (FastRenderMod.isEnabled() && FastRenderMod.AvoidRedundantFramebufferSwitching) {
			GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
		}
	}

	@Redirect(method = "writeToBuffer", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/opengl/DirectStateAccess;bufferSubData(IJLjava/nio/ByteBuffer;I)V"))
	private void fixSlowBufferUploadOnAppleGpu(DirectStateAccess instance, int buffer, long offset, ByteBuffer data, int usage, @Local(argsOnly = true) GpuBufferSlice gpuBufferSlice) {
		if (FastRenderMod.isEnabled() && FastRenderMod.FixSlowBufferUploadOnAppleGpu && IS_APPLE_SILICON && offset == 0 && gpuBufferSlice.length() == gpuBufferSlice.buffer().size()) {
			instance.bufferData(buffer, data, usage);
		} else {
			instance.bufferSubData(buffer, offset, data, usage);
		}
	}

}