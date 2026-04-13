package silence.simsool.lucentclient.handler;

import net.minecraft.util.Mth;

public class TPSHandler {
	private static long lastPacketTime = -1L;
	private static float currentTps = 20.0f;

	public static void onTimePacket() {
		long now = System.currentTimeMillis();
		if (lastPacketTime != -1L) {
			long elapsed = now - lastPacketTime;
			if (elapsed < 1) elapsed = 1;
			float tps = 20000f / (float) elapsed;
			float cappedTps = Mth.clamp(tps, 0.0f, 20.0f);
			currentTps = Mth.lerp(0.2f, currentTps, cappedTps);
		} lastPacketTime = now;
	}

	public static float getTPS() {
		return currentTps;
	}

	public static String getTextTPS() {
		return String.format("%.1f", currentTps);
	}

	public static void reset() {
		lastPacketTime = -1L;
		currentTps = 20.0f;
	}
}