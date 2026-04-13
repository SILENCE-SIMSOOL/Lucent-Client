package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import silence.simsool.lucentclient.handler.ServerHandler;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

	@Inject(method = "handleSetTime", at = @At("HEAD"))
	private void onHandleSetTime(ClientboundSetTimePacket packet, CallbackInfo ci) {
		ServerHandler.onTimePacket();
	}

	@Inject(method = "handlePongResponse", at = @At("HEAD"))
	private void onHandlePongResponse(ClientboundPongResponsePacket packet, CallbackInfo ci) {
		ServerHandler.onPingPacket(packet.time());
	}

}