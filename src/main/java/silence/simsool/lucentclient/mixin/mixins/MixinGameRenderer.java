package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import silence.simsool.lucent.ui.utils.UAnimation;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;
import silence.simsool.lucentclient.mods.impl.graphics.HurtCamMod;
import silence.simsool.lucentclient.mods.impl.utility.ZoomMod;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

	@Unique
	private float currentZoom = 1.0f;

	@Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
	private void onGetFov(Camera camera, float f, boolean bl, CallbackInfoReturnable<Float> cir) {
		if (ZoomMod.isEnabled()) {
			float targetZoom = ZoomMod.getTargetZoom();
			if (ZoomMod.SmoothZoom) currentZoom = UAnimation.lerp(currentZoom, targetZoom, 0.2f);
			else currentZoom = targetZoom;
			if (currentZoom != 1.0f) cir.setReturnValue(cir.getReturnValue() / currentZoom);
		}
	}

	@Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
	private void onBobHurt(PoseStack poseStack, float f, CallbackInfo ci) {
		if (HurtCamMod.isEnabled() && HurtCamMod.DisableHurtCam) ci.cancel();
	}

	@WrapOperation(method = { "tick", "renderLevel" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getEffectBlendFactor(Lnet/minecraft/core/Holder;F)F"))
	private float onGetEffectBlendFactor(LocalPlayer player, Holder<MobEffect> effect, float partialTick, Operation<Float> original) {
		if (AnimationsMod.isEnabled() && AnimationsMod.HideNausea && effect.equals(MobEffects.NAUSEA)) {
			return 0.0f;
		}
		return original.call(player, effect, partialTick);
	}

}