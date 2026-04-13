package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import silence.simsool.lucentclient.handler.TPSHandler;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

	@Inject(method = "handleSetTime", at = @At("HEAD"))
	private void onHandleSetTime(ClientboundSetTimePacket packet, CallbackInfo ci) {
		TPSHandler.onTimePacket();
	}

}