package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class FPSMod extends Mod {

	public FPSMod() {
		super("FPS", "Displays your current frames per second.", "HUD", "fps, frames", LucentClientUtils.getModIcon("fps"));
	}

	@ModConfig(type = ConfigType.SWITCH, name = "Text Shadow", priority = 1000)
	public static boolean TextShadow = true;

	@ModConfig(type = ConfigType.COLOR, name = "Text Color", priority = 990)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(type = ConfigType.SWITCH, name = "Show Background", priority = 980)
	public static boolean ShowBackground = false;

	@ModConfig(type = ConfigType.COLOR, name = "Background Color", priority = 970)
	public static int BackgroundColor = 0x80000000;

	@ModConfig(type = ConfigType.SWITCH, name = "Reverse Order", priority = 960)
	public static boolean ReverseOrder = false;

	@ModConfig(type = ConfigType.SWITCH, name = "Show Brackets", priority = 950)
	public static boolean ShowBrackets = false;

}
