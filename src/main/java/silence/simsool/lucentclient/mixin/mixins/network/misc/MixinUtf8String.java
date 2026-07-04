package silence.simsool.lucentclient.mixin.mixins.network.misc;

import java.nio.charset.StandardCharsets;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.Utf8String;
import net.minecraft.network.VarInt;
import silence.simsool.lucentclient.mods.impl.performance.network.NetworkFixMod;

@Mixin(Utf8String.class)
public class MixinUtf8String {

	/**
	 * @author SimSool (ported from Andrew Steinborn/Krypton)
	 * @reason Optimized UTF-8 writing for network performance.
	 */
	@Overwrite
	public static void write(ByteBuf buf, CharSequence string, int length) {
		if (NetworkFixMod.isEnabled() && NetworkFixMod.FastUtf8Encoding) {
			if (string.length() > length) throw new EncoderException("String too big (was " + string.length() + " characters, max " + length + ")");
			int utf8Bytes = ByteBufUtil.utf8Bytes(string);
			int maxBytesPermitted = ByteBufUtil.utf8MaxBytes(length);
			if (utf8Bytes > maxBytesPermitted) throw new EncoderException("String too big (was " + utf8Bytes + " bytes encoded, max " + maxBytesPermitted + ")");
			else {
				VarInt.write(buf, utf8Bytes);
				buf.writeCharSequence(string, StandardCharsets.UTF_8);
			}
		}
		else {
			int utf8Bytes = ByteBufUtil.utf8Bytes(string);
			if (utf8Bytes > length * 3) throw new EncoderException("String too big");
			VarInt.write(buf, utf8Bytes);
			buf.writeCharSequence(string, StandardCharsets.UTF_8);
		}
	}

}