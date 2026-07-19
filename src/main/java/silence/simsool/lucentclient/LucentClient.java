package silence.simsool.lucentclient;

import static silence.simsool.lucent.Lucent.config;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.config.api.LucentAPI;
import silence.simsool.lucent.events.impl.LucentEvent;
import silence.simsool.lucent.general.utils.useful.UChat;
import silence.simsool.lucentclient.commands.BasicCommand;
import silence.simsool.lucentclient.handler.ServerHandler;
import silence.simsool.lucentclient.huds.HUDRegister;
import silence.simsool.lucentclient.init.Keybinds;
import silence.simsool.lucentclient.mods.LucentClientModRegister;
import silence.simsool.lucentclient.mods.impl.performance.network.NetworkFixMod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class LucentClient implements ClientModInitializer {

	public static final String ID = "lucentclient";
	public static final String NAME = "Lucent Client";
	public static final String VERSION = "1.0.19";

	public static final String PREFIX = "§b[§fLucent Client§b] ";

	public static String LATEST_VERSION = "Fetching...";
	private String MOD_URL = "https://silencedev.kro.kr/en/products/lucent-client";

	static {
		NetworkFixMod.configureNettyMemory();
	}

	@Override
	public void onInitializeClient() {

		Lucent.LOG.info("Initializing LucentClient...");

		updateLatestVersion();

		ClientPlayConnectionEvents.INIT.register((handler, client) -> {
			if (!LATEST_VERSION.equals("Unknown") && !LATEST_VERSION.equals("Fetching...")) {
				if (isVersionOlder(VERSION, LATEST_VERSION)) {
					client.execute(() -> {
						UChat.chat(PREFIX + "§cA new version of Lucent Client is available!");
						UChat.chat(PREFIX + "§cCurrent: §e" + VERSION + " §7| §cLatest: §a" + LATEST_VERSION);
						
						MutableComponent downloadMsg = Component.literal(PREFIX + "§ePlease download the latest version from: §b§n" + MOD_URL);
						downloadMsg.withStyle(style -> style
							.withClickEvent(new ClickEvent.OpenUrl(URI.create(MOD_URL)))
							.withHoverEvent(new HoverEvent.ShowText(Component.literal("§eClick to download latest version")))
						);
						UChat.chat(downloadMsg);
					});
				}
			}
		});

		LucentClientModRegister.register(config);
		HUDRegister.register(LucentAPI.getHUDManager());
		Keybinds.init();
		ServerHandler.init();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			BasicCommand.register(dispatcher);
		});

		LucentEvent.INIT_FINISHED_EVENT.register(() -> {
			LucentClientUtils.initLoadedMods();
		});

		LucentEvent.SERVER_JOIN_EVENT.register(() -> {
			if (LucentClientUtils.loadedKrypton || LucentClientUtils.loadedFerritecore || LucentClientUtils.loadedEntityCulling) {
				StringBuilder sb = new StringBuilder();
				sb.append("\n§bLucent Client §fcomes with several advanced optimization features built in.\n\n");
				sb.append("§7The following mods provide features already included in §bLucent Client, §7resulting in §funnecessary resource usage §7and §fredundant processing§7.\n\n");
				sb.append("§eRecommend removing them:\n");

				boolean hasDuplicateMod = false;

				if (LucentClientUtils.loadedKrypton) {
					sb.append(" §7- §cKrypton\n");
					hasDuplicateMod = true;
				}
				if (LucentClientUtils.loadedFerritecore) {
					sb.append(" §7- §cFerriteCore\n");
					hasDuplicateMod = true;
				}
				if (LucentClientUtils.loadedEntityCulling) {
					sb.append(" §7- §cEntityCulling\n");
					hasDuplicateMod = true;
				}

				if (hasDuplicateMod) UChat.chat(sb.toString().trim() + "\n");
			}
		});

	}

	private static void updateLatestVersion() {
		HttpClient.newHttpClient().sendAsync(
				HttpRequest.newBuilder(URI.create("https://api.github.com/repos/SILENCE-SIMSOOL/Lucent-Client/releases/latest")).build(),
				HttpResponse.BodyHandlers.ofString()
		).thenAccept(res -> {
			try {
				if (res.statusCode() == 200) {
					String body = res.body();
					int idx = body.indexOf("\"tag_name\":");
					if (idx != -1) {
						String value = body.substring(idx + 11);
						int quoteStart = value.indexOf("\"") + 1;
						int quoteEnd = value.indexOf("\"", quoteStart);
						String version = value.substring(quoteStart, quoteEnd);
						if (version.startsWith("v")) version = version.substring(1);
						LATEST_VERSION = version;
					} else LATEST_VERSION = "Unknown";
				} else LATEST_VERSION = "Unknown";
			} catch(Exception e){
				LATEST_VERSION = "Unknown";
			}
		});
	}

	private static boolean isVersionOlder(String localVersion, String remoteVersion) {
		try {
			String[] localParts = localVersion.split("\\.");
			String[] remoteParts = remoteVersion.split("\\.");
			int length = Math.max(localParts.length, remoteParts.length);
			for (int i = 0; i < length; i++) {
				int localPart = i < localParts.length ? Integer.parseInt(localParts[i].trim()) : 0;
				int remotePart = i < remoteParts.length ? Integer.parseInt(remoteParts[i].trim()) : 0;
				if (localPart < remotePart) return true;
				if (localPart > remotePart) return false;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

}