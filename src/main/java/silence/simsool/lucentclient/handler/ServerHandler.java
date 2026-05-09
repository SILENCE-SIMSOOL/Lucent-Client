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

	public static void init() {
		PacketEvent.RECEIVE.register(event -> {
			if (event.packet instanceof ClientboundPongResponsePacket pong) onPingPacket(pong.time());
		});
		LucentEvent.SERVER_TICK_EVENT.register(ServerHandler::onTimePacket);
		LucentEvent.SERVER_DISCONNECT_EVENT.register(ServerHandler::reset);
	}

	public static void onTimePacket() {
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

	public static void onPingPacket(long sendTime) {
		currentPing = (int) Math.max(0, Util.getMillis() - sendTime);
		var pingLog = mc.getDebugOverlay().getPingLogger();
		int sampleSize = Math.min(pingLog.size(), 20);
		if (sampleSize == 0) {
			averagePing = currentPing;
			return;
		}
		long total = 0;
		for (int i = 0; i < sampleSize; i++) total += pingLog.get(i);
		averagePing = (int) (total / sampleSize);
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

	public static void reset() {
		lastPacketTime = -1L;
		currentTps = 20.0f;
		currentPing = 0;
		averagePing = 0;
	}

}