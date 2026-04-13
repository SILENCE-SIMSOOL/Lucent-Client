package silence.simsool.lucentclient.mixin.mixins;

import java.awt.Color;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.textures.GpuTextureView;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import silence.simsool.lucentclient.mods.impl.graphics.AnimationsMod;

@Mixin(OverlayTexture.class)
public class MixinOverlayTexture {

	@Shadow
	@Final
	private DynamicTexture texture;

	private int lastDamageColor = -1;
	private boolean wasCustomColor = false;

	@Inject(method = "getTextureView", at = @At("HEAD"))
	private void onGetTextureView(CallbackInfoReturnable<GpuTextureView> cir) {
		if (AnimationsMod.isEnabled() && AnimationsMod.UseDamageColor) {
			int dInt = AnimationsMod.DamageColor.getRGB();

			if (!wasCustomColor || lastDamageColor != dInt) {
				updateTexture(AnimationsMod.DamageColor);
				lastDamageColor = dInt;
				wasCustomColor = true;
			}
		} else if (wasCustomColor) {
			// 바닐라 기본 흰색(white)으로 복구
			updateTexture(null);
			wasCustomColor = false;
		}
	}

	private void updateTexture(Color color) {
		NativeImage pixels = this.texture.getPixels();
		if (pixels == null) return;

		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				if (y < 8) {
					pixels.setPixel(x, y, -1291911168); // 바닐라 기본값
				} else {
					int alpha;
					int colorVal;
					
					if (color != null) {
						// 커스텀 색상 적용
						alpha = (int) ((1.0F - (float) x / 15.0F * 0.75F) * color.getAlpha());
						colorVal = ARGB.color(alpha, color.getRed(), color.getGreen(), color.getBlue());
					} else {
						// 바닐라 기본 흰색 적용
						alpha = (int) ((1.0F - (float) x / 15.0F * 0.75F) * 255.0F);
						colorVal = ARGB.white(alpha);
					}
					pixels.setPixel(x, y, colorVal);
				}
			}
		}
		this.texture.upload();
	}
}