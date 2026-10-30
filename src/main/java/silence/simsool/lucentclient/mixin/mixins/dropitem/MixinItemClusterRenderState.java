package silence.simsool.lucentclient.mixin.mixins.dropitem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import silence.simsool.lucentclient.ducks.ItemClusterRenderStateDuck;
import silence.simsool.lucentclient.mods.impl.graphics.DropItemMod;

@Mixin(ItemClusterRenderState.class)
public class MixinItemClusterRenderState implements ItemClusterRenderStateDuck {

	@Unique
	private float lucent$scale = 1.0f;

	@ModifyReturnValue(method = "getRenderedAmount", at = @At("RETURN"))
	private static int forceStackAmount(int original, int stackCount) {
		if (DropItemMod.isEnabled() && DropItemMod.DropStackCount != 0) {
			return Math.min(DropItemMod.DropStackCount, stackCount);
		}
		return original;
	}

	@Inject(method = "extractItemGroupRenderState", at = @At("HEAD"))
	private void onExtractItemGroupRenderState(Entity entity, ItemStack stack, ItemModelResolver itemModelResolver, CallbackInfo ci) {
		float scale = 1.0f;
		if (DropItemMod.isEnabled()) {
			scale = (float) DropItemMod.ItemScale;
			if (DropItemMod.UHCOverlay > 0.0) {
				String id = stack.getItem().toString();
				if (id.contains("apple") || id.contains("gold") || id.contains("player_head")) {
					scale = (float) DropItemMod.UHCOverlay;
				}
			}
		}
		lucent$setScale(scale);
	}

	@Override
	public float lucent$getScale() {
		return lucent$scale;
	}

	@Override
	public void lucent$setScale(float scale) {
		this.lucent$scale = scale;
	}

}