package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.utils.LucentCategory;

import java.util.ArrayList;
import java.util.List;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.events.impl.LucentEvent;
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
				"lucent.config.lucentclient.cpsmod.general.name", "lucent.config.lucentclient.cpsmod.general.description",
				LucentCategory.HUB,
				"click",
				LucentClientUtils.getModIcon("cps")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(CPSMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.cpsmod.property.showbrackets.name", 
		description = "lucent.config.lucentclient.cpsmod.property.showbrackets.description",
		priority = 2
	)
	public static boolean ShowBrackets = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.cpsmod.property.reverseorder.name", 
		description = "lucent.config.lucentclient.cpsmod.property.reverseorder.description",
		priority = 1
	)
	public static boolean ReverseOrder = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.cpsmod.property.textshadow.name", 
		description = "lucent.config.lucentclient.cpsmod.property.textshadow.description",
		category = "Text Style",
		priority = 2
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "lucent.config.lucentclient.cpsmod.property.textcolor.name", 
		description = "lucent.config.lucentclient.cpsmod.property.textcolor.description",
		category = "Text Style",
		priority = 1
	)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.cpsmod.property.showbackground.name", 
		description = "lucent.config.lucentclient.cpsmod.property.showbackground.description",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = false;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "lucent.config.lucentclient.cpsmod.property.backgroundcolor.name", 
		description = "lucent.config.lucentclient.cpsmod.property.backgroundcolor.description",
		category = "Background",
		priority = 1
	)
	public static int BackgroundColor = 0x80000000;

	private static final List<Long> leftClicks = new ArrayList<>();
	private static final List<Long> rightClicks = new ArrayList<>();

	@Override
	public void onLeftClickPost(LucentEvent.LeftClickPostEvent event) {
		leftClicks.add(System.currentTimeMillis());
	}

	@Override
	public void onRightClickPost(LucentEvent.RightClickPostEvent event) {
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