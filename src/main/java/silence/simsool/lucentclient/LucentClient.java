package silence.simsool.lucentclient;

import static silence.simsool.lucent.Lucent.config;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
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
import silence.simsool.lucentclient.updater.AutoUpdater;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class LucentClient implements ClientModInitializer {

	public static final String ID = "lucentclient";
	public static final String NAME = "Lucent Client";
	public static final String VERSION = "1.1.5";

	public static final String PREFIX = "§b[§fLucent Client§b] ";

	public static final String MC_VERSION = "26.2";
	public static String LATEST_VERSION = "Fetching...";

	public static int MAX_CHAT_HISTORY = 512; //32767;

	static {
		NetworkFixMod.configureNettyMemory();
	}

	@Override
	public void onInitializeClient() {

		AutoUpdater.cleanOldJars();

		Lucent.LOG.info("Initializing LucentClient...");

		LucentClientModRegister.register(config);
		HUDRegister.register(LucentAPI.getHUDManager());

		AutoUpdater.checkAndUpdate();
		Keybinds.init();
		ServerHandler.init();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			BasicCommand.register(dispatcher);
		});

		LucentEvent.INIT_FINISHED_EVENT.register(() -> {
			LucentClientUtils.initLoadedMods();
		});

		LucentEvent.SERVER_JOIN_EVENT.register(() -> {
			if (LucentClientUtils.loadedKrypton || LucentClientUtils.loadedFerritecore
					|| LucentClientUtils.loadedEntityCulling) {
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

				if (hasDuplicateMod)
					UChat.chat(sb.toString().trim() + "\n");
			}
		});

	}

}