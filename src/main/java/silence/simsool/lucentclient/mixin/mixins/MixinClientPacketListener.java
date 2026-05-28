package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.multiplayer.ClientPacketListener;
import silence.simsool.lucentclient.mods.impl.hud.PingMod;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;showNetworkCharts()Z"))
	private boolean alwaysSendPing(boolean original) {
		if (PingMod.isEnabled()) return true;
		return original;
	}

}