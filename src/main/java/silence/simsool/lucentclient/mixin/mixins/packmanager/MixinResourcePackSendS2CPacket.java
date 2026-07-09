package silence.simsool.lucentclient.mixin.mixins.packmanager;

import static silence.simsool.lucent.Lucent.mc;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import silence.simsool.lucentclient.mods.impl.utility.packmanager.PackManagerMod;

@Mixin(ClientboundResourcePackPushPacket.class)
public abstract class MixinResourcePackSendS2CPacket {

	@Shadow
	public abstract UUID id();

	@Inject(method = "handle(Lnet/minecraft/network/protocol/common/ClientCommonPacketListener;)V", at = @At("HEAD"), cancellable = true)
	private void apply(ClientCommonPacketListener listener, CallbackInfo ci) {
		if (PackManagerMod.isEnabled() && PackManagerMod.BlockPackDownload) {
			ServerData serverData = mc.getCurrentServer();
			if (serverData != null && serverData.getResourcePackStatus() == ServerData.ServerPackStatus.DISABLED) {
				if (mc.getConnection() != null) {
					mc.getConnection().send(new ServerboundResourcePackPacket(this.id(), ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
				}
				ci.cancel();
			}
		}
	}

}