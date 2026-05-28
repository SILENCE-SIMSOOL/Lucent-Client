package silence.simsool.lucentclient.mods.impl.utility;

import org.lwjgl.glfw.GLFW;

import net.minecraft.util.Mth;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.data.KeyBind;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.useful.UScreen;
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

	private static float scrollFactor = 1.0f;

	public static boolean isZoomKeyDown() {
		return ZoomKey.isKeyDown() && UScreen.isScreenClose();
	}

	public static float getTargetZoom() {
		return isZoomKeyDown() ? ZoomFactor : 1.0f;
	}

	public static boolean handleScroll(double amount) {
		if (isEnabled() && isZoomKeyDown()) {
			if (amount > 0) scrollFactor += 0.5f;
			else if (amount < 0) scrollFactor -= 0.5f;

			scrollFactor = Mth.clamp(scrollFactor, 1.0f, 10.0f);
			return true;
		}

		scrollFactor = 1.0f;
		return false;
	}

}