package silence.simsool.lucentclient.mixin.mixins.network.pipeline;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.network.LegacyQueryHandler;
import silence.simsool.lucentclient.mods.impl.performance.network.NetworkFixMod;

@Mixin(LegacyQueryHandler.class)
public abstract class MixinLegacyQueryHandler {

	@Inject(method = "channelRead", at = @At(value = "HEAD"), cancellable = true)
	public void channelRead(ChannelHandlerContext ctx, Object msg, CallbackInfo ci) throws Exception {
		if (NetworkFixMod.isEnabled() && NetworkFixMod.LegacyQueryFix) {
			if (!ctx.channel().isActive()) {
				if (msg instanceof ByteBuf buf) {
					buf.clear();
				}
				ci.cancel();
			}
		}
	}

}