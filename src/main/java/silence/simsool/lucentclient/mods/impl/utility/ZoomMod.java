package silence.simsool.lucentclient.mods.impl.utility;

import org.lwjgl.glfw.GLFW;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.data.KeyBind;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class ZoomMod extends Mod {

	public ZoomMod() {
		super("Zoom", "Allows you to zoom in using a keybind.", "Utility", "zoom, optifine, vision", "lucid:zoom");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(ZoomMod.class).isEnabled;
	}

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "Zoom Key",
		description = "Key used to zoom in.",
		category = "General",
		priority = 1000
	)
	public static KeyBind ZoomKey = KeyBind.ofKey(GLFW.GLFW_KEY_C, 0);

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Zoom Factor",
		description = "How much to zoom in when the key is pressed.",
		category = "General",
		min = 1.0,
		max = 10.0,
		step = 0.5,
		priority = 990
	)
	public static double ZoomFactor = 4.0;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Smooth Zoom",
		description = "Whether to smoothly animate the zoom transition.",
		category = "General",
		priority = 980
	)
	public static boolean SmoothZoom = true;

}