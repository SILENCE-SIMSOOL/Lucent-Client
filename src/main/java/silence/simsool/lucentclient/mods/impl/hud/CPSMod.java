package silence.simsool.lucentclient.mods.impl.hud;

import java.util.ArrayList;
import java.util.List;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Text Style", priority = 500)
@ModConfig.CategoryPriority(name = "Background", priority = 100)
public class CPSMod extends Mod {

	public CPSMod() {
		super(
				"CPS", "Displays your current clicks per second.",
				"HUD",
				"click",
				LucentClientUtils.getModIcon("cps")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(CPSMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Brackets", 
		description = "Encloses the CPS value in brackets on the screen.",
		category = "General",
		priority = 2
	)
	public static boolean ShowBrackets = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Reverse Order", 
		description = "Swaps the display order of left and right clicks on the screen.",
		category = "General",
		priority = 1
	)
	public static boolean ReverseOrder = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Text Shadow", 
		description = "Adds a shadow effect to the CPS text on the screen.",
		category = "Text Style",
		priority = 2
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "Text Color", 
		description = "Sets the color of the CPS text displayed on the screen.",
		category = "Text Style",
		priority = 1
	)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Background", 
		description = "Displays a background box behind the CPS text on the screen.",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = false;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "Background Color", 
		description = "Sets the color of the background box displayed on the screen.",
		category = "Background",
		priority = 1
	)
	public static int BackgroundColor = 0x80000000;

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
