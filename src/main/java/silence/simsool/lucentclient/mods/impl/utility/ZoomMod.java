package silence.simsool.lucentclient.mods.impl.utility;

import static silence.simsool.lucent.Lucent.mc;
import org.lwjgl.glfw.GLFW;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.data.KeyBind;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class ZoomMod extends Mod {

	public ZoomMod() {
		super(
				"Zoom", "Allows you to zoom in using a keybind.",
				"Utility",
				"",
				LucentClientUtils.getModIcon("zoom")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(ZoomMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Smooth Zoom",
		description = "Whether to smoothly animate the zoom transition.",
		category = "General"
	)
	public static boolean SmoothZoom = true;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Zoom Factor",
		description = "How much to zoom in when the key is pressed.",
		category = "General",
		min = 1.0, max = 10.0, step = 0.5
	)
	public static float ZoomFactor = 4.0f;

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "Zoom Key",
		description = "Key used to zoom in.",
		category = "Keybind"
	)
	public static KeyBind ZoomKey = KeyBind.ofKey(GLFW.GLFW_KEY_C, 0);

	public static boolean isZoomKeyDown() {
		return ZoomKey.isKeyDown() && mc.screen == null;
	}

	public static float getTargetZoom() {
		return isZoomKeyDown() ? ZoomFactor : 1.0f;
	}

}
