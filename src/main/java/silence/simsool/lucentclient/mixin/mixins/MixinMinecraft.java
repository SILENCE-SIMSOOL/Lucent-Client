package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

	@Redirect(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V", ordinal = 0))
	private void redirectSwing(LocalPlayer player, InteractionHand hand) {
		if (!(AnimationsMod.isEnabled() && AnimationsMod.DisableEntityClickAnimation)) {
			player.swing(hand);
		}
	}

}