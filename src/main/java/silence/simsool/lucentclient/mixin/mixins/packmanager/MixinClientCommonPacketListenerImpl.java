package silence.simsool.lucentclient.mixin.mixins.packmanager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import silence.simsool.lucentclient.mods.impl.utility.packmanager.PackManagerMod;

@Mixin(ClientCommonPacketListenerImpl.class)
public class MixinClientCommonPacketListenerImpl {

	@Shadow
	@Final
	private Connection connection;

	@Inject(method = "handleResourcePackPush", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V", shift = At.Shift.AFTER), cancellable = true)
	private void onResourcePack(ClientboundResourcePackPushPacket packet, CallbackInfo ci) {
		if (PackManagerMod.isEnabled()) {
			if (!packet.url().contains("hypixel.net") || !packet.url().contains("SkyBlock")) return;
			PackManagerMod.addPack(packet.id());
			if (!PackManagerMod.BlockPackDownload) return;
			connection.send(new ServerboundResourcePackPacket(packet.id(), ServerboundResourcePackPacket.Action.ACCEPTED));
			connection.send(new ServerboundResourcePackPacket(packet.id(), ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
			ci.cancel();
		}
	}

	@Inject(method = "handleResourcePackPop", at = @At("TAIL"))
	private void onResourcePackPop(ClientboundResourcePackPopPacket packet, CallbackInfo ci) {
		if (PackManagerMod.isEnabled()) {
			PackManagerMod.removePack(packet.id().orElse(null));
		}
	}

	@Inject(method = "onDisconnect", at = @At("HEAD"))
	private void onDisconnect(DisconnectionDetails details, CallbackInfo ci) {
		if (PackManagerMod.isEnabled()) {
			PackManagerMod.clearPacks();
		}
	}

}