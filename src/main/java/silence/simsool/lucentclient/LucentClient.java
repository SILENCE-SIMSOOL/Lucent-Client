package silence.simsool.lucentclient;

import static silence.simsool.lucent.Lucent.config;

import net.fabricmc.api.ClientModInitializer;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.hud.HUDManager;
import silence.simsool.lucentclient.huds.HUDRegister;
import silence.simsool.lucentclient.mods.ModRegister;

public class LucentClient implements ClientModInitializer {

	public static final String ID = "lucentclient";
	public static final String NAME = "Lucent Client";
	public static final String VERSION = "0.0.1";

	@Override
	public void onInitializeClient() {
		Lucent.LOG.info("Initializing LucentClient...");
		ModRegister.register(config);
		HUDRegister.register(HUDManager.INSTANCE);
		
		config.loadGlobalConfig();
		config.loadConfigs();
		HUDManager.INSTANCE.loadAll();
	}

}