package silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips;

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
		super("Scrollable Tooltips", "Allows you to scroll long item tooltips.", "Utility", "scroll, tooltip, item", LucentClientUtils.getModIcon("scrollable_tooltips"));
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(ScrollableTooltipsMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Enable WASD Scrolling",
		description = "Allows moving the tooltip using the W, A, S, and D keys.",
		category = "General"
	)
	public static boolean UseWASD = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Use Left Shift for Horizontal Scroll",
		description = "Hold the Left Shift key to switch the mouse wheel scroll direction to horizontal.",
		category = "General"
	)
	public static boolean UseLShift = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Reset Position on Unlock",
		description = "Automatically resets the tooltip's scroll position when it is no longer being actively updated.",
		category = "General"
	)
	public static boolean ResetOnUnlock = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Matrix Transformation Mode",
		description = "Uses matrix transforms for tooltip movement. Better compatibility, but may cause rare artifacts.",
		category = "Matrix"
	)
	public static boolean MatrixMode = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Auto-Adjust to Top",
		description = "Prevents tooltips from being cut off by automatically shifting them downward if they are too close to the top edge. (Only works when Matrix Mode is disabled)",
		category = "Matrix",
		parent = "Enable Matrix Transformation Mode"
	)
	public static boolean StartOnTop = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Invert Horizontal Scroll",
		description = "Inverts the direction of horizontal scrolling.",
		category = "Invert",
		priority = 2
	)
	public static boolean InvertXScroll = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Invert Vertical Scroll",
		description = "Inverts the direction of vertical scrolling.",
		category = "Invert",
		priority = 1
	)
	public static boolean InvertYScroll = false;

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "Move Up",
		description = "Key used to scroll the tooltip up.",
		category = "Keybind"
	)
	public static KeyBind moveUp = KeyBind.ofKey(GLFW.GLFW_KEY_UP, 0);

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "Move Down",
		description = "Key used to scroll the tooltip down.",
		category = "Keybind"
	)
	public static KeyBind moveDown = KeyBind.ofKey(GLFW.GLFW_KEY_DOWN, 0);

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "Reset",
		description = "Key used to reset the tooltip scroll position.",
		category = "Keybind"
	)
	public static KeyBind reset = KeyBind.ofKey(GLFW.GLFW_KEY_UNKNOWN, 0);

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "Horizontal Scroll Modifier",
		description = "Key held to scroll horizontally instead of vertically.",
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