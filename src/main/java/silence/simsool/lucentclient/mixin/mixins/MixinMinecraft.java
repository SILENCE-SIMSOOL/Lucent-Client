package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

	@Redirect(
		method = "startUseItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"
		)
	)
	private void redirectSwing(LocalPlayer player, InteractionHand hand) {
		if (!(AnimationsMod.isEnabled() && AnimationsMod.DisableUseAnimation)) {
			player.swing(hand);
		}
	}

	@Redirect(
		method = "startUseItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;itemUsed(Lnet/minecraft/world/InteractionHand;)V"
		)
	)
	private void redirectItemUsed(ItemInHandRenderer itemInHandRenderer, InteractionHand hand) {
		if (!(AnimationsMod.isEnabled() && AnimationsMod.DisableUseAnimation)) {
			itemInHandRenderer.itemUsed(hand);
		}
	}

}
