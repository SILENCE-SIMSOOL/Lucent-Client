package silence.simsool.lucentclient.mods.impl.utility.chatting;

import static silence.simsool.lucent.Lucent.mc;

import silence.simsool.lucentclient.mods.impl.utility.ChattingMod;

public class SmoothChat {

	private static final float LINE_HEIGHT = 9.0f;
	private static long startNanos = 0L;
	private static float durationMs = 0.0f;
	private static boolean animating = false;

	public static void start() {
		if (!ChattingMod.isEnabled() || !ChattingMod.SmoothChat) {
			animating = false;
			return;
		}
		durationMs = ChattingMod.SmoothChatDuration;
		animating = durationMs > 0.0f;
		startNanos = System.nanoTime();
	}

	private static float percent() {
		if (!animating) return 1.0f;
		if (durationMs <= 0.0f) {
			animating = false;
			return 1.0f;
		}
		float elapsed = (System.nanoTime() - startNanos) / 1_000_000.0f;
		if (elapsed >= durationMs) {
			animating = false;
			return 1.0f;
		}
		float x = elapsed / durationMs;
		float inv = x - 1.0f;
		return -1.0f * inv * inv * inv * inv + 1.0f;
	}

	public static float translateY(boolean scrolled) {
		if (!ChattingMod.isEnabled() || !ChattingMod.SmoothChat || scrolled || !animating) return 0.0f;
		float p = percent();
		if (p >= 1.0f) return 0.0f;
		float scale = mc.options.chatScale().get().floatValue();
		return LINE_HEIGHT * (1.0f - p) * scale;
	}

}