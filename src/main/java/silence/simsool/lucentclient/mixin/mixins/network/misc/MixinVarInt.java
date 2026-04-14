package silence.simsool.lucentclient.mixin.mixins.network.misc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.VarInt;
import silence.simsool.lucentclient.mods.impl.performance.network.NetworkFixMod;
import silence.simsool.lucentclient.mods.impl.performance.network.utils.VarIntUtil;

@Mixin(VarInt.class)
public class MixinVarInt {

	/**
	 * @author SILENCE
	 * @reason Optimized VarInt size calculation.
	 */
	@Overwrite
	public static int getByteSize(int v) {
		if (NetworkFixMod.isEnabled() && NetworkFixMod.FastVarInt) {
			return VarIntUtil.getVarIntLength(v);
		}
		// Vanilla fallback
		for (int i = 1; i < 5; ++i) {
			if ((v & -1 << i * 7) == 0) return i;
		}
		return 5;
	}

	/**
	 * @author Lucent
	 * @reason Optimized VarInt writing using bitwise operations.
	 */
	@Overwrite
	public static ByteBuf write(ByteBuf buf, int value) {
		if (NetworkFixMod.isEnabled() && NetworkFixMod.FastVarInt) {
			if ((value & (0xFFFFFFFF << 7)) == 0) buf.writeByte(value);
			else if ((value & (0xFFFFFFFF << 14)) == 0) {
				int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
				buf.writeShort(w);
			}
			else writeVarIntFull(buf, value);
			return buf;
		}

		// Vanilla fallback
		while (true) {
			if ((value & -128) == 0) {
				buf.writeByte(value);
				return buf;
			}
			buf.writeByte(value & 127 | 128);
			value >>>= 7;
		}
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