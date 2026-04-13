package silence.simsool.lucentclient.utils;

import java.lang.reflect.Method;

import net.fabricmc.loader.api.FabricLoader;

public class LucentClientUtils {

	private static boolean hasCheckedSilenceUtils = false;
	private static boolean hasSilenceUtils = false;
	private static Method isInDungeonMethod = null;

	public static String getModIcon(String modName) {
		return "/assets/lucentclient/textures/modicons/" + modName + ".png";
	}

	public static boolean checkInDungeon() {
		if (!hasCheckedSilenceUtils) {
			hasSilenceUtils = FabricLoader.getInstance().isModLoaded("silenceutils");
			if (hasSilenceUtils) {
				try {
					Class<?> utilsClass = Class.forName("silence.simsool.silenceutils.utils.Utils");
					isInDungeonMethod = utilsClass.getMethod("isInDungeon");
				} catch (Exception e) {
					hasSilenceUtils = false;
				}
			}
			hasCheckedSilenceUtils = true;
		}

		if (hasSilenceUtils && isInDungeonMethod != null) {
			try {
				return (boolean) isInDungeonMethod.invoke(null);
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

}