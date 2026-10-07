package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(FirstPersonHandsAndItems.class)
public class MixinFirstPersonHandsAndItems {

	@Inject(
		method = "shouldInstantlyReplaceVisibleItem",
		at = @At("HEAD"),
		cancellable = true
	)
	private void onShouldInstantlyReplaceVisibleItem(ItemStack itemStack, ItemStack itemStack2, LocalPlayer localPlayer, CallbackInfoReturnable<Boolean> cir) {
		if (AnimationsMod.isEnabled() && AnimationsMod.NoEquipReset) {
			if (ItemStack.isSameItem(itemStack, itemStack2)) {
				cir.setReturnValue(true);
			}
		}
	}

	@WrapOperation(
		method = "tick",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemSwapScale(F)F")
	)
	private float onGetItemSwapScale(LocalPlayer instance, float partialTick, Operation<Float> original) {
		if (AnimationsMod.isEnabled() && (AnimationsMod.NoEquipReset || AnimationsMod.InPlaceSwing)) return 1.0f;
		return original.call(instance, partialTick);
	}
}
