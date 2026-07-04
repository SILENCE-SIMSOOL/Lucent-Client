package silence.simsool.lucentclient.mixin.mixins.network.pipeline.pretender;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import io.netty.channel.ChannelOutboundHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.LocalFrameEncoder;
import net.minecraft.network.Varint21LengthFieldPrepender;
import silence.simsool.lucentclient.mods.impl.performance.network.NetworkFixMod;
import silence.simsool.lucentclient.mods.impl.performance.network.pipeline.MinecraftVarintPrepender;

@Mixin(Connection.class)
public class MixinConnection {

	/**
	 * @author SimSool (ported from Andrew Steinborn/Krypton)
	 * @reason replace Mojang prepender with a more efficient one
	 */
	@Overwrite
	private static ChannelOutboundHandler createFrameEncoder(boolean local) {
		if (local) return new LocalFrameEncoder();
		else return (NetworkFixMod.isEnabled() && NetworkFixMod.FastVarintPrepender) ? MinecraftVarintPrepender.INSTANCE : new Varint21LengthFieldPrepender();
	}

}