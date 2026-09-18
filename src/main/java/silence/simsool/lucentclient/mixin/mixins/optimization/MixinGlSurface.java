package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.opengl.GlSurface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(GlSurface.class)
public abstract class MixinGlSurface {

	@Inject(method = "present", at = @At("HEAD"))
	private void unbindFramebufferBeforeSwappingBuffers(final CallbackInfo ci) {
		if (FastRenderMod.isEnabled() && FastRenderMod.AvoidFramebufferSwitching) {
			GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
		}
	}

}