package silence.simsool.lucentclient;

import static silence.simsool.lucent.Lucent.config;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.config.api.LucentAPI;
import silence.simsool.lucentclient.commands.BasicCommand;
import silence.simsool.lucentclient.huds.HUDRegister;
import silence.simsool.lucentclient.init.Keybinds;
import silence.simsool.lucentclient.mods.ModRegister;

public class LucentClient implements ClientModInitializer {

	public static final String ID = "lucentclient";
	public static final String NAME = "Lucent Client";
	public static final String VERSION = "0.0.1";

	public static final String PREFIX = "§b[§fLucent Client§b] ";

	@Override
	public void onInitializeClient() {
		Lucent.LOG.info("Initializing LucentClient...");

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			BasicCommand.register(dispatcher);
		});

		ModRegister.register(config);
		HUDRegister.register(LucentAPI.getHUDManager());
		Keybinds.init();

	}

}