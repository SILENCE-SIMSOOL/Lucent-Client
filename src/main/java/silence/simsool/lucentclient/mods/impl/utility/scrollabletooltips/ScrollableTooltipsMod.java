package silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips;

import silence.simsool.lucent.general.utils.LucentCategory;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.data.KeyBind;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.useful.UScreen;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Matrix", priority = 500)
@ModConfig.CategoryPriority(name = "Invert", priority = 250)
@ModConfig.CategoryPriority(name = "Keybind", priority = 100)
public class ScrollableTooltipsMod extends Mod {

	public ScrollableTooltipsMod() {
		super(
				"lucent.config.lucentclient.scrollabletooltipsmod.general.name", "lucent.config.lucentclient.scrollabletooltipsmod.general.description",
				LucentCategory.UTILITY, "scroll, tooltip, item", LucentClientUtils.getModIcon("scrollable_tooltips"));
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(ScrollableTooltipsMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.usewasd.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.usewasd.description"
	)
	public static boolean UseWASD = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.uselshift.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.uselshift.description"
	)
	public static boolean UseLShift = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.resetonunlock.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.resetonunlock.description"
	)
	public static boolean ResetOnUnlock = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.matrixmode.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.matrixmode.description",
		category = "Matrix"
	)
	public static boolean MatrixMode = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.startontop.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.startontop.description",
		category = "Matrix",
		parent = "!MatrixMode"
	)
	public static boolean StartOnTop = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.invertxscroll.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.invertxscroll.description",
		category = "Invert",
		priority = 2
	)
	public static boolean InvertXScroll = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.invertyscroll.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.invertyscroll.description",
		category = "Invert",
		priority = 1
	)
	public static boolean InvertYScroll = false;

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.moveup.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.moveup.description",
		category = "Keybind"
	)
	public static KeyBind moveUp = KeyBind.ofKey(GLFW.GLFW_KEY_UP, 0);

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.movedown.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.movedown.description",
		category = "Keybind"
	)
	public static KeyBind moveDown = KeyBind.ofKey(GLFW.GLFW_KEY_DOWN, 0);

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.reset.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.reset.description",
		category = "Keybind"
	)
	public static KeyBind reset = KeyBind.ofKey(GLFW.GLFW_KEY_UNKNOWN, 0);

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.scrollabletooltipsmod.property.horizontal.name",
		description = "lucent.config.lucentclient.scrollabletooltipsmod.property.horizontal.description",
		category = "Keybind"
	)
	public static KeyBind horizontal = KeyBind.ofKey(GLFW.GLFW_KEY_LEFT_SHIFT, 0);

	public static boolean shouldExecute() {
		Screen screen = UScreen.getScreen();

		if (screen instanceof CreativeModeInventoryScreen) {
			return false;
		}

		if (screen instanceof AbstractContainerScreen) {
			return true;
		}

		return false;
	}

}