package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(ScreenEffectRenderer.class)
public class MixinScreenEffectRenderer {

	@Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
	private static void onRenderFire(PoseStack poseStack, MultiBufferSource bufferSource, TextureAtlasSprite sprite, CallbackInfo ci) {
		if (AnimationsMod.isEnabled()) {
			if (!AnimationsMod.FireOverlay) ci.cancel();
			else if (AnimationsMod.FireHeight != 0.5) poseStack.translate(0.0F, (float) (AnimationsMod.FireHeight - 0.5), 0.0F);
		}
	}

}