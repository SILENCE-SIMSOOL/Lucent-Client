package silence.simsool.lucentclient.utils;

import java.lang.reflect.Method;

import net.fabricmc.loader.api.FabricLoader;

public class LucentClientUtils {

	public static boolean loadedSilenceUtils = false;
	public static boolean loadedKrypton = false;
	public static boolean loadedFerritecore = false;
	public static boolean loadedEntityCulling = false;

	public static void initLoadedMods() {
		loadedSilenceUtils = FabricLoader.getInstance().isModLoaded("silenceutils");
		loadedKrypton = FabricLoader.getInstance().isModLoaded("krypton");
		loadedFerritecore = FabricLoader.getInstance().isModLoaded("ferritecore");
		loadedEntityCulling = FabricLoader.getInstance().isModLoaded("entityculling");
	}

	public static String getModIcon(String modName) {
		return "/assets/lucentclient/textures/modicons/" + modName + ".png";
	}

	public static boolean checkInDungeon() {
		if (loadedSilenceUtils) {
			try {
				Class<?> utilsClass = Class.forName("silence.simsool.silenceutils.utils.Utils");
				Method isInDungeonMethod = utilsClass.getMethod("isInDungeon");
				return (boolean) isInDungeonMethod.invoke(null);
			} catch (Exception e) {
				loadedSilenceUtils = false;
				return false;
			}
		}
		return false;
	}

}