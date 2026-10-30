package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import silence.simsool.lucentclient.mods.impl.graphics.BetterNightVisionMod;
import silence.simsool.lucentclient.mods.impl.graphics.ColorSaturationMod;
import silence.simsool.lucentclient.mods.impl.graphics.HurtCamMod;
import silence.simsool.lucentclient.mods.impl.graphics.colorsaturation.SaturationHandler;

import silence.simsool.lucentclient.mods.impl.graphics.damagetint.DamageTintHelper;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

	@Shadow
	@Final
	private Minecraft minecraft;
	@Shadow
	@Final
	private CrossFrameResourcePool resourcePool;
	@Shadow
	@Final
	private RenderTarget mainRenderTarget;

	@Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
	private void onBobHurt(CameraRenderState cameraRenderState, PoseStack poseStack, CallbackInfo ci) {
		if (HurtCamMod.isEnabled() && HurtCamMod.DisableHurtCam) ci.cancel();
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		DamageTintHelper.updateIfChanged();
	}

	@ModifyReturnValue(method = "nightVisionScale", at = @At("RETURN"))
	private static float cleanerNightVision(float original, @Local(argsOnly = true) LivingEntity entity) {
		if (BetterNightVisionMod.isEnabled()) {
			if (BetterNightVisionMod.DisableNightVision) {
				return 0.0f;
			}
			if (entity != null) {
				MobEffectInstance effect = entity.getEffect(MobEffects.NIGHT_VISION);
				if (effect != null && BetterNightVisionMod.CleanerNightVision) {
					return !effect.endsWithin(200) ? original : (float) effect.getDuration() / 200.0f;
				}
			}
		}
		return original;
	}

	@Inject(
		method = "render()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/LevelRenderer;blitEntityOutline()V"
		)
	)
	private void onApplySaturation(CallbackInfo ci) {
		if (!ColorSaturationMod.isEnabled() || !this.minecraft.isGameLoadFinished() || this.minecraft.level == null) {
			SaturationHandler.free();
			return;
		}
		SaturationHandler.render(this.mainRenderTarget, this.resourcePool);
	}

}