package silence.simsool.lucentclient.mixin.mixins;

import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.core.component.DataComponents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

	@Shadow
	private ItemStack mainHandItem;

	@WrapOperation(
		method = "renderHandsWithItems",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackAnim(F)F")
	)
	private float onGetAttackAnim(LocalPlayer instance, float partialTick, Operation<Float> original) {
		if (!AnimationsMod.isEnabled()) return original.call(instance, partialTick);
		return AnimationsMod.getSwingAnimation(partialTick);
	}

	@Inject(
		method = "shouldInstantlyReplaceVisibleItem",
		at = @At("HEAD"),
		cancellable = true
	)
	private void onShouldInstantlyReplaceVisibleItem(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<Boolean> cir) {
		if (AnimationsMod.isEnabled() && AnimationsMod.NoEquipReset) {
			if (ItemStack.isSameItem(itemStack, itemStack2)) {
				cir.setReturnValue(true);
			}
		}
	}

	@Inject(
		method = "renderHandsWithItems",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
			ordinal = 0
		)
	)
	private void onApplyTransformations(float f, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LocalPlayer localPlayer, int i, CallbackInfo ci) {
		if (!AnimationsMod.isEnabled()) return;
		if (mainHandItem.isEmpty()) return;
		if (mainHandItem.has(DataComponents.MAP_ID) && !AnimationsMod.ChangeHoldingMap) return;
		AnimationsMod.applyTransformations(poseStack);
	}

	@Inject(
		method = "renderItem",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V")
	)
	private void onRenderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci) {
		if (!AnimationsMod.isEnabled()) return;
		if (itemStack.getItem() instanceof ShieldItem && AnimationsMod.ShieldHeight != 0.0f) {
			poseStack.translate(0, (float) AnimationsMod.ShieldHeight, 0);
		}
		AnimationsMod.applyScale(poseStack);
	}

	@Inject(
		method = "renderMapHand",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/ClientAsset$Texture;texturePath()Lnet/minecraft/resources/Identifier;")
	)
	private void onRenderMapHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, HumanoidArm humanoidArm, CallbackInfo ci) {
		if (!AnimationsMod.isEnabled()) return;
		if (!AnimationsMod.ChangeHoldingMap) return;
		AnimationsMod.applyScale(poseStack);
	}

	@Inject(
		method = "renderMap",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitCustomGeometry(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/SubmitNodeCollector$CustomGeometryRenderer;)V")
	)
	private void onRenderMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, ItemStack itemStack, CallbackInfo ci) {
		if (!AnimationsMod.isEnabled()) return;
		if (!AnimationsMod.ChangeHoldingMap) return;
		poseStack.translate(64f, 64f, 0f);
		AnimationsMod.applyScale(poseStack);
		poseStack.translate(-64f, -64f, 0f);
	}

	@WrapOperation(
		method = "tick",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemSwapScale(F)F")
	)
	private float onGetItemSwapScale(LocalPlayer instance, float partialTick, Operation<Float> original) {
		if (AnimationsMod.isEnabled() && (AnimationsMod.NoEquipReset || AnimationsMod.InPlaceSwing)) return 1.0f;
		return original.call(instance, partialTick);
	}

	@WrapWithCondition(
		method = "renderHandsWithItems",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V")
	)
	private boolean onHandSway(PoseStack instance, Quaternionfc quaternionfc) {
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