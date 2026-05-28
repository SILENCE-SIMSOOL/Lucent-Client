package silence.simsool.lucentclient.handler;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import silence.simsool.lucent.events.impl.LucentEvent;
import silence.simsool.lucent.events.impl.PacketEvent;

public class ServerHandler {

	private static long lastPacketTime = -1L;
	private static float currentTps = 20.0f;
	private static int currentPing = 0;
	private static int averagePing = 0;
	private static long lastPingCalculationTime = 0L;

	public static void init() {
		PacketEvent.RECEIVE.register(event -> {
			if (event.packet instanceof ClientboundPongResponsePacket pong) {
				updatePing(pong.time());
			}
		});
		LucentEvent.SERVER_TICK_EVENT.register(ServerHandler::updateTPS);
		LucentEvent.SERVER_DISCONNECT_EVENT.register(ServerHandler::reset);
	}

	public static float getTPS() {
		return currentTps;
	}

	public static String getTextTPS() {
		return String.format("%.1f", currentTps);
	}

	public static int getPing() {
		return currentPing;
	}

	public static int getAveragePing() {
		return averagePing;
	}

	public static void updateTPS() {
		long now = System.currentTimeMillis();
		if (lastPacketTime != -1L) {
			long elapsed = now - lastPacketTime;
			if (elapsed < 1) elapsed = 1;
			float tps = 20000f / (float) elapsed;
			float cappedTps = Mth.clamp(tps, 0.0f, 20.0f);
			currentTps = Mth.lerp(0.2f, currentTps, cappedTps);
		}
		lastPacketTime = now;
	}

	public static void updatePing(long sendTime) {
		currentPing = (int) Math.max(0, Util.getMillis() - sendTime);

		long now = System.currentTimeMillis();
		if (now - lastPingCalculationTime >= 3500L) {
			lastPingCalculationTime = now;

			var pingLog = mc.getDebugOverlay().getPingLogger();
			int totalSize = pingLog.size();
			int sampleSize = Math.min(totalSize, 20);
			if (sampleSize == 0) {
				averagePing = currentPing;
				return;
			}
			long total = 0;
			for (int i = 0; i < sampleSize; i++) total += pingLog.get(totalSize - 1 - i);
			averagePing = (int) (total / sampleSize);
		}
	}

	public static void reset() {
		lastPacketTime = -1L;
		currentTps = 20.0f;
		currentPing = 0;
		averagePing = 0;
		lastPingCalculationTime = 0L;
	}

}