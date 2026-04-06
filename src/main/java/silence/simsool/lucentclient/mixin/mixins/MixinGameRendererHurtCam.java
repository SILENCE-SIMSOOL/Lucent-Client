package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.GameRenderer;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(GameRenderer.class)
public class MixinGameRendererHurtCam {

	@Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
	private void onBobHurt(PoseStack poseStack, float f, CallbackInfo ci) {
		if (AnimationsMod.isEnabled() && AnimationsMod.NoHurtCam) {
			ci.cancel();
		}
	}

}