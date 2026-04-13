package silence.simsool.lucentclient.mixin.mixins.network.misc;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.VarInt;
import silence.simsool.lucentclient.utils.VarIntUtil;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(VarInt.class)
public class MixinVarInt {

	@Overwrite
	public static int getByteSize(int v) {
		return VarIntUtil.getVarIntLength(v);
	}

	@Overwrite
	public static ByteBuf write(ByteBuf buf, int value) {
		if ((value & (0xFFFFFFFF << 7)) == 0) buf.writeByte(value);
		else if ((value & (0xFFFFFFFF << 14)) == 0) {
			int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
			buf.writeShort(w);
		}
		else writeVarIntFull(buf, value);
		return buf;
	}

	private static void writeVarIntFull(ByteBuf buf, int value) {
		if ((value & (0xFFFFFFFF << 7)) == 0) {
			buf.writeByte(value);
		} else if ((value & (0xFFFFFFFF << 14)) == 0) {
			int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
			buf.writeShort(w);
		} else if ((value & (0xFFFFFFFF << 21)) == 0) {
			int w = (value & 0x7F | 0x80) << 16 | ((value >>> 7) & 0x7F | 0x80) << 8 | (value >>> 14);
			buf.writeMedium(w);
		} else if ((value & (0xFFFFFFFF << 28)) == 0) {
			int w = (value & 0x7F | 0x80) << 24 | (((value >>> 7) & 0x7F | 0x80) << 16) | ((value >>> 14) & 0x7F | 0x80) << 8 | (value >>> 21);
			buf.writeInt(w);
		} else {
			int w = (value & 0x7F | 0x80) << 24 | ((value >>> 7) & 0x7F | 0x80) << 16 | ((value >>> 14) & 0x7F | 0x80) << 8 | ((value >>> 21) & 0x7F | 0x80);
			buf.writeInt(w);
			buf.writeByte(value >>> 28);
		}
	}

}