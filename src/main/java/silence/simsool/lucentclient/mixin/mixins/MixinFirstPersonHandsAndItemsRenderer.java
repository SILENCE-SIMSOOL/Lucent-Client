package silence.simsool.lucentclient.mixin.mixins;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class MixinFirstPersonHandsAndItemsRenderer {

	@WrapOperation(
		method = "submitHandsWithItems",
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;swingAnimation:F", opcode = Opcodes.GETFIELD)
	)
	private float onGetAttackAnim(AvatarRenderState instance, Operation<Float> original) {
		if (!AnimationsMod.isEnabled()) return original.call(instance);
		return AnimationsMod.getSwingAnimation(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false));
	}

	@Inject(
		method = "submitHandsWithItems",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;submitArmWithItem(Lnet/minecraft/client/renderer/state/level/PlayerRenderState;Lnet/minecraft/client/renderer/state/level/FirstPersonHandsAndItemsRenderState;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
			ordinal = 0
		)
	)
	private void onApplyTransformations(float f, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, PlayerRenderState playerRenderState, FirstPersonHandsAndItemsRenderState handState, CallbackInfo ci) {
		if (!AnimationsMod.isEnabled()) return;
		if (handState.mainHandItem.isEmpty()) return;
		if (handState.mainHandItem.has(DataComponents.MAP_ID) && !AnimationsMod.ChangeHoldingMap) return;
		AnimationsMod.applyTransformations(poseStack);
	}

	@Inject(
		method = "submitArmWithItem",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V")
	)
	private void onRenderItem(PlayerRenderState playerRenderState, FirstPersonHandsAndItemsRenderState handState, float f, float g, InteractionHand hand, float h, ItemStack itemStack, float j, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int k, CallbackInfo ci) {
		if (!AnimationsMod.isEnabled()) return;
		if (itemStack.getItem() instanceof ShieldItem && AnimationsMod.ShieldHeight != 0.0f) {
			poseStack.translate(0, (float) AnimationsMod.ShieldHeight, 0);
		}
		AnimationsMod.applyScale(poseStack);
	}

	@Inject(
		method = "renderMapHand",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER)
	)
	private void onRenderMapHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, HumanoidArm humanoidArm, PlayerRenderState playerRenderState, CallbackInfo ci) {
		if (!AnimationsMod.isEnabled()) return;
		if (!AnimationsMod.ChangeHoldingMap) return;
		AnimationsMod.applyScale(poseStack);
	}

	@Inject(
		method = "renderMap",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitCustomGeometry(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/SubmitNodeCollector$CustomGeometryRenderer;)V")
	)
	private void onRenderMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, ItemStack itemStack, boolean flag, FirstPersonHandsAndItemsRenderState state, CallbackInfo ci) {
		if (!AnimationsMod.isEnabled()) return;
		if (!AnimationsMod.ChangeHoldingMap) return;
		poseStack.translate(64f, 64f, 0f);
		AnimationsMod.applyScale(poseStack);
		poseStack.translate(-64f, -64f, 0f);
	}

	@WrapWithCondition(
		method = "submitHandsWithItems",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;rotateDegrees(Lcom/mojang/math/Axis;F)V")
	)
	private boolean onHandSway(PoseStack instance, Axis axis, float degrees) {
		return !(AnimationsMod.isEnabled() && AnimationsMod.NoHandSway);
	}

	@ModifyVariable(
		method = "renderPlayerArm",
		at = @At(value = "STORE"),
		ordinal = 4
	)
	private float onRenderPlayerArmX(float f) {
		return (AnimationsMod.isEnabled() && AnimationsMod.InPlaceSwing) ? 0.0f : f;
	}

	@ModifyVariable(
		method = "renderPlayerArm",
		at = @At(value = "STORE"),
		ordinal = 5
	)
	private float onRenderPlayerArmY(float f) {
		return (AnimationsMod.isEnabled() && AnimationsMod.InPlaceSwing) ? 0.0f : f;
	}

	@ModifyVariable(
		method = "renderPlayerArm",
		at = @At(value = "STORE"),
		ordinal = 6
	)
	private float onRenderPlayerArmZ(float f) {
		return (AnimationsMod.isEnabled() && AnimationsMod.InPlaceSwing) ? 0.0f : f;
	}

	@WrapOperation(
		method = "renderTwoHandedMap",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 0)
	)
	private void onRenderTwoHandedMapTranslate(PoseStack instance, float f, float g, float h, Operation<Void> original) {
		if (AnimationsMod.isEnabled() && AnimationsMod.InPlaceSwing) return;
		original.call(instance, f, g, h);
	}

	@WrapOperation(
		method = "renderOneHandedMap",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 2)
	)
	private void onRenderOneHandedMapTranslate(PoseStack instance, float f, float g, float h, Operation<Void> original) {
		if (AnimationsMod.isEnabled() && AnimationsMod.InPlaceSwing) return;
		original.call(instance, f, g, h);
	}

	@WrapOperation(
		method = "swingArm",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V")
	)
	private void onSwingArmTranslate(PoseStack instance, float f, float g, float h, Operation<Void> original) {
		if (AnimationsMod.isEnabled() && AnimationsMod.InPlaceSwing) return;
		original.call(instance, f, g, h);
	}
}
