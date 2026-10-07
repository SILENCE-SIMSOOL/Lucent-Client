package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import silence.simsool.lucentclient.LucentClient;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

	@Redirect(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/component/SwingAnimation;Z)Z", ordinal = 0))
	private boolean redirectSwing(LocalPlayer player, InteractionHand hand, net.minecraft.world.item.component.SwingAnimation animation, boolean flag) {
		if (!(AnimationsMod.isEnabled() && AnimationsMod.DisableEntityClickAnimation)) {
			return player.swing(hand, animation, flag);
		}
		return false;
	}

	@Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
	private void onCreateTitle(CallbackInfoReturnable<String> cir) {
		cir.setReturnValue("Lucent Client - mc" + LucentClient.MC_VERSION + " (v" + LucentClient.VERSION + ")");
	}

}