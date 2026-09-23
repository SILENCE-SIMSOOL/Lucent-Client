package silence.simsool.lucentclient.utils;

import java.lang.reflect.Method;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import silence.simsool.lucent.general.utils.useful.UScreen;

public class LucentClientUtils {

	public static boolean loadedSilenceUtils = false;
	public static boolean loadedKrypton = false;
	public static boolean loadedFerritecore = false;
	public static boolean loadedEntityCulling = false;
	public static boolean loadedFlashback = false;
	private static Method isInDungeonMethod = null;

	public static void initLoadedMods() {
		loadedSilenceUtils = FabricLoader.getInstance().isModLoaded("silenceutils");
		loadedKrypton = FabricLoader.getInstance().isModLoaded("krypton");
		loadedFerritecore = FabricLoader.getInstance().isModLoaded("ferritecore");
		loadedEntityCulling = FabricLoader.getInstance().isModLoaded("entityculling");
		loadedFlashback = FabricLoader.getInstance().isModLoaded("flashback");

		if (loadedSilenceUtils) {
			try {
				Class<?> utilsClass = Class.forName("silence.simsool.silenceutils.utils.Utils");
				isInDungeonMethod = utilsClass.getMethod("isInDungeon");
			} catch (Exception e) {
				loadedSilenceUtils = false;
				isInDungeonMethod = null;
			}
		}
	}

	public static String getModIcon(String modName) {
		return "/assets/lucentclient/textures/modicons/" + modName + ".png";
	}

	public static boolean checkInDungeon() {
		if (loadedSilenceUtils && isInDungeonMethod != null) {
			try {
				return (boolean) isInDungeonMethod.invoke(null);
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public static void openFlashbackReplays(Screen parentScreen) {
		if (!loadedFlashback) return;
		try {
			Class<?> clazz = Class.forName("com.moulberry.flashback.screen.select_replay.SelectReplayScreen");
			Screen replayScreen = (Screen) clazz.getConstructor(Screen.class).newInstance(parentScreen);
			UScreen.setScreen(replayScreen);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}