package silence.simsool.lucentclient.features.fastserverping;

import io.netty.buffer.ByteBuf;

public class FSPVarIntUtils {

	public static void writeVarInt(ByteBuf b, int i) {
		while ((i & -128) != 0) {
			b.writeByte(i & 127 | 128);
			i >>>= 7;
		}
		b.writeByte(i);
	}

	public static int readVarInt(ByteBuf b) {
		int n = 0, s = 0;
		byte c;
		do {
			c = b.readByte();
			n |= (c & 127) << s;
			s += 7;
		} while ((c & 128) != 0);
		return n;
	}
}