package silence.simsool.lucentclient.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import silence.simsool.lucent.general.utils.UChat;
import silence.simsool.lucentclient.LucentClient;
import silence.simsool.lucentclient.handler.ServerHandler;

public class BasicCommand {

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {

		dispatcher.register(ClientCommandManager.literal("lc")
			.then(ClientCommandManager.literal("tps").executes(context -> displayTps()))
			.then(ClientCommandManager.literal("ping").executes(context -> displayPing()))
		);

		dispatcher.register(ClientCommandManager.literal("tps")
			.executes(context -> displayTps())
		);

		dispatcher.register(ClientCommandManager.literal("ping")
			.executes(context -> displayPing())
		);

	}

	private static int displayTps() {
		float tps = ServerHandler.getTPS();
		String color = tps >= 18.0 ? "§a" : (tps >= 13.0 ? "§e" : "§c");
		UChat.chat(LucentClient.PREFIX + "§fCurrent TPS: " + color + String.format("%.1f", tps));
		return 1;
	}

	private static int displayPing() {
		int ping = ServerHandler.getAveragePing();
		String color = ping < 100 ? "§a" : (ping < 200 ? "§e" : "§c");
		UChat.chat(LucentClient.PREFIX + "§fCurrent Ping: " + color + ping + "ms");
		return 1;
	}

}