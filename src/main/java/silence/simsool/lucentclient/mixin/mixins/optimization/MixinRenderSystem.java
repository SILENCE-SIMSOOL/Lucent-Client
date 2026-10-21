package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silence.simsool.lucentclient.features.fastrender.ByteBufferBuilderPool;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(RenderSystem.class)
public abstract class MixinRenderSystem {

	@Inject(method = "flipFrame", at = @At("HEAD"))
	private static void onFlipFrame(CallbackInfo ci) {
		if (FastRenderMod.isEnabled() && FastRenderMod.AvoidRedundantFramebufferSwitching) {
			GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
		}
		if (FastRenderMod.isEnabled() && FastRenderMod.EnhancedBatching) {
			ByteBufferBuilderPool.onEndFrame();
		}
	}

}