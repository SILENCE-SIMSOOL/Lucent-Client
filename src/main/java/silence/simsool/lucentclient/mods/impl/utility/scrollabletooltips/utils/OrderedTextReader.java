package silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips.utils;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

public class OrderedTextReader {
	private static class Visitor implements FormattedCharSink {
		private int finalIndex = -1;
		private final StringBuilder outputBuilder = new StringBuilder();

		public Visitor() {}

		public boolean accept(int index, Style style, int codePoint) {
			if (index > this.finalIndex) this.finalIndex = index;
			else return false;
			outputBuilder.append((char) codePoint);
			return true;
		}

		public String getString() {
			return outputBuilder.toString();
		}
	}

	public static String read(FormattedCharSequence text) {
		Visitor visitor = new Visitor();
		text.accept(visitor);
		return visitor.getString();
	}
}