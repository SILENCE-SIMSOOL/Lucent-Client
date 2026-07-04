package silence.simsool.lucentclient.mixin.mixins.network.pipeline;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.util.ByteProcessor;
import net.minecraft.network.VarInt;
import net.minecraft.network.Varint21FrameDecoder;
import silence.simsool.lucentclient.mods.impl.performance.network.NetworkFixMod;
import silence.simsool.lucentclient.mods.impl.performance.network.utils.WellKnownExceptions;

@Mixin(Varint21FrameDecoder.class)
public class MixinVarint21FrameDecoder {

	/**
	 * @author SimSool (ported from Andrew Steinborn/Krypton)
	 * @reason Highly optimized packet frame decoding.
	 */
	@Overwrite
	public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (NetworkFixMod.isEnabled() && NetworkFixMod.FastFrameDecoding) {
			if (!ctx.channel().isActive()) {
				in.clear();
				return;
			}

			int packetStart = in.forEachByte(ByteProcessor.FIND_NON_NUL);
			if (packetStart == -1) {
				in.clear();
				return;
			}
			in.readerIndex(packetStart);

			in.markReaderIndex();
			int preIndex = in.readerIndex();
			int length = readRawVarInt21(in);
			if (preIndex == in.readerIndex()) return;
			if (length < 0) throw WellKnownExceptions.BAD_LENGTH_CACHED;

			if (length > 0) {
				if (in.readableBytes() < length) in.resetReaderIndex();
				else out.add(in.readRetainedSlice(length));
			}
		} else {
			// Vanilla Fallback
			in.markReaderIndex();
			byte[] abyte = new byte[3];
			for (int i = 0; i < abyte.length; ++i) {
				if (!in.isReadable()) {
					in.resetReaderIndex();
					return;
				}
				abyte[i] = in.readByte();
				if (abyte[i] >= 0) {
					int length = VarInt.read(Unpooled.wrappedBuffer(abyte));
					if (in.readableBytes() < length) {
						in.resetReaderIndex();
						return;
					}
					out.add(in.readRetainedSlice(length));
					return;
				}
			}
			throw new CorruptedFrameException("length wider than 21-bit");
		}
	}

	@Unique
	private static int readRawVarInt21(ByteBuf buffer) {
		if (buffer.readableBytes() < 4) return readRawVarintSmallBuf(buffer);
		int wholeOrMore = buffer.getIntLE(buffer.readerIndex());

		int atStop = ~wholeOrMore & 0x808080;
		if (atStop == 0) throw WellKnownExceptions.VARINT_BIG_CACHED;

		int bitsToKeep = Integer.numberOfTrailingZeros(atStop) + 1;
		buffer.skipBytes(bitsToKeep >> 3);

		int preservedBytes = wholeOrMore & (atStop ^ (atStop - 1));
		preservedBytes = (preservedBytes & 0x007F007F) | ((preservedBytes & 0x00007F00) >> 1);
		preservedBytes = (preservedBytes & 0x00003FFF) | ((preservedBytes & 0x3FFF0000) >> 2);
		return preservedBytes;
	}

	@Unique
	private static int readRawVarintSmallBuf(ByteBuf buffer) {
		if (!buffer.isReadable()) return 0;
		buffer.markReaderIndex();

		byte tmp = buffer.readByte();
		if (tmp >= 0) return tmp;
		int result = tmp & 0x7F;
		if (!buffer.isReadable()) {
			buffer.resetReaderIndex();
			return 0;
		}
		if ((tmp = buffer.readByte()) >= 0) return result | tmp << 7;
		result |= (tmp & 0x7F) << 7;
		if (!buffer.isReadable()) {
			buffer.resetReaderIndex();
			return 0;
		}
		if ((tmp = buffer.readByte()) >= 0) return result | tmp << 14;
		return result | (tmp & 0x7F) << 14;
	}

}