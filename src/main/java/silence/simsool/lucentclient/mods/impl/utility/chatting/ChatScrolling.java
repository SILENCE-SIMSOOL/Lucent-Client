package silence.simsool.lucentclient.mods.impl.utility.chatting;

import silence.simsool.lucentclient.mods.impl.utility.ChattingMod;

public class ChatScrolling {

	public static boolean shouldSmooth = false;
	private static float current = 0.0f;
	private static float from = 0.0f;
	private static float to = 0.0f;
	private static long startNanos = 0L;
	private static float durationMs = 150.0f;
	private static boolean initialized = false;
	private static int frozen = 0;

	public static void step(int actual) {
		if (!ChattingMod.isEnabled() || !ChattingMod.SmoothScrolling) {
			current = actual;
			to = current;
			frozen = actual;
			shouldSmooth = false;
			return;
		}
		if (!initialized) {
			current = actual;
			to = current;
			initialized = true;
		}
		if ((float) actual != to) {
			if (shouldSmooth && Math.abs(actual - current) > 1.0f) {
				from = current;
				to = actual;
				durationMs = 150.0f;
				startNanos = System.nanoTime();
			} else {
				from = actual;
				to = actual;
				current = actual;
			}
		}
		shouldSmooth = false;
		if (durationMs <= 0.0f) {
			current = to;
		} else {
			float x = Math.min(1.0f, Math.max(0.0f, (System.nanoTime() - startNanos) / 1_000_000.0f / durationMs));
			if (x >= 1.0f) current = to;
			else current = from + (to - from) * (1.0f - (1.0f - x) * (1.0f - x));
		}
		frozen = Math.round(current);
	}

	public static void shift(int delta) {
		if (!ChattingMod.isEnabled() || !ChattingMod.SmoothScrolling || !initialized) return;
		from = Math.max(0.0f, from + delta);
		to = Math.max(0.0f, to + delta);
		current = Math.max(0.0f, current + delta);
		frozen = Math.round(current);
	}

	public static int pos() {
		return Math.max(0, frozen);
	}

}