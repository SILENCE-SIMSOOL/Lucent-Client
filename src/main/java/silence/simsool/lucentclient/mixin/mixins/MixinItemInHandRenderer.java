package silence.simsool.lucentclient.mixin.mixins;

import static silence.simsool.lucent.Lucent.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

	@Shadow
	private ItemStack mainHandItem;

	@Shadow
	private ItemStack offHandItem;

	@Inject(method = "renderItem", at = @At("HEAD"))
	private void onRenderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector bufferSource, int i, CallbackInfo ci) {
		if (AnimationsMod.isEnabled()) {
			if (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
				float scale = (float) AnimationsMod.ItemScale;
				if (scale != 1.0f) {
					poseStack.scale(scale, scale, scale);
				}
			}
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		if (AnimationsMod.isEnabled()) {
			if (mc.player == null) return;
			ItemStack newMainHand = mc.player.getMainHandItem();
			ItemStack newOffHand = mc.player.getOffhandItem();

			if (isSameItem(this.mainHandItem, newMainHand)) this.mainHandItem = newMainHand;
			if (isSameItem(this.offHandItem, newOffHand)) this.offHandItem = newOffHand;
		}
	}

	@Unique
	private boolean isSameItem(ItemStack oldStack, ItemStack newStack) {
		if (oldStack == newStack) return true;
		if (oldStack.isEmpty() || newStack.isEmpty()) return false;
		return oldStack.getItem() == newStack.getItem();
	}

}