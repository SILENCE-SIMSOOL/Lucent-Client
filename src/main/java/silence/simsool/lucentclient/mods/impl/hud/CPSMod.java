package silence.simsool.lucentclient.mods.impl.hud;

import java.util.ArrayList;
import java.util.List;

import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class CPSMod extends Mod {

	public CPSMod() {
		super("CPS", "Displays your current clicks per second.", "HUD", "cps, clicks", LucentClientUtils.getModIcon("cps"));
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

	private static final List<Long> leftClicks = new ArrayList<>();

	private static final List<Long> rightClicks = new ArrayList<>();

	public static synchronized void addLeftClick() {
		leftClicks.add(System.currentTimeMillis());
	}

	public static synchronized void addRightClick() {
		rightClicks.add(System.currentTimeMillis());
	}

	public static synchronized int getLeftCPS() {
		long now = System.currentTimeMillis();
		leftClicks.removeIf(timestamp -> now - timestamp > 1000);
		return leftClicks.size();
	}

	public static synchronized int getRightCPS() {
		long now = System.currentTimeMillis();
		rightClicks.removeIf(timestamp -> now - timestamp > 1000);
		return rightClicks.size();
	}
}