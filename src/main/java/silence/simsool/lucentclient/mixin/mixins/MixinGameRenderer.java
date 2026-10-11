package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;
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

	@Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true, remap = false)
	private void onBobHurt(CameraRenderState cameraRenderState, PoseStack poseStack, CallbackInfo ci) {
		if (HurtCamMod.isEnabled() && HurtCamMod.DisableHurtCam) ci.cancel();
	}

	@WrapOperation(method = { "tick", "renderLevel" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getEffectBlendFactor(Lnet/minecraft/core/Holder;F)F"))
	private float onGetEffectBlendFactor(LocalPlayer player, Holder<MobEffect> effect, float partialTick, Operation<Float> original) {
		if (AnimationsMod.isEnabled() && AnimationsMod.HideNausea && effect.equals(MobEffects.NAUSEA)) {
			return 0.0f;
		}
		return original.call(player, effect, partialTick);
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
		method = "render(Lnet/minecraft/client/DeltaTracker;Z)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V"
		)
	)
	private void onApplySaturation(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
		if (!ColorSaturationMod.isEnabled() || !this.minecraft.isGameLoadFinished() || this.minecraft.level == null) {
			SaturationHandler.free();
			return;
		}
		SaturationHandler.render(this.mainRenderTarget, this.resourcePool);
	}
}