package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(targets = "com.mojang.blaze3d.opengl.GlCommandEncoder")
public abstract class MixinGlCommandEncoder {

	@Redirect(method = "submitRenderPass", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/opengl/GlStateManager;_glBindFramebuffer(II)V"))
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

}