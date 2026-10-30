package silence.simsool.lucentclient.mods.impl.graphics.damagetint;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

public class DamageTintHelper {

	private static DynamicTexture texture;
	private static boolean lastCustomHitColor;
	private static int lastHitColor;

	public static void init(DynamicTexture dynamicTexture) {
		texture = dynamicTexture;
		updateOverlayColor(true);
	}

	public static void updateIfChanged() {
		updateOverlayColor(false);
	}

	private static void updateOverlayColor(boolean force) {
		if (texture == null) {
			return;
		}

		boolean custom = AnimationsMod.isEnabled() && AnimationsMod.CustomHitColor;
		int color = AnimationsMod.HitColor != null ? AnimationsMod.HitColor.getRGB() : 0;

		if (!force && custom == lastCustomHitColor && color == lastHitColor) {
			return;
		}

		lastCustomHitColor = custom;
		lastHitColor = color;

		NativeImage image = texture.getPixels();
		if (image == null) {
			return;
		}

		int defaultColor = -1291911168;
		int targetColor;
		if (custom) {
			int r = (color >> 16) & 0xFF;
			int g = (color >> 8) & 0xFF;
			int b = color & 0xFF;
			int userAlpha = (color >>> 24) & 0xFF;
			int overlayAlpha = 255 - userAlpha;
			targetColor = (overlayAlpha << 24) | (r << 16) | (g << 8) | b;
		} else {
			targetColor = defaultColor;
		}

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 8; y++) {
				image.setPixel(x, y, targetColor);
			}
		}
		texture.upload();
	}

}