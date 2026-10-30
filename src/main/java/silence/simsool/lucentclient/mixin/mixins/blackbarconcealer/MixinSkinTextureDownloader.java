package silence.simsool.lucentclient.mixin.mixins.blackbarconcealer;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkinTextureDownloader.class)
public abstract class MixinSkinTextureDownloader {

	@Unique
	private static final Rect2i[] ARM_DIFF = {
		new Rect2i(50, 16, 2, 4),
		new Rect2i(54, 20, 2, 12),
		new Rect2i(42, 48, 2, 4),
		new Rect2i(46, 52, 2, 12)
	};

	@Shadow
	@Final
	private TextureManager textureManager;

	@Inject(method = {"lambda$registerTextureInManager$0", "method_65864"}, at = @At("HEAD"), cancellable = true, require = 0)
	private void registerTextureInManager(ClientAsset.Texture texture, NativeImage nativeImage, CallbackInfoReturnable<ClientAsset.Texture> cir) {
		Identifier texturePath = texture.texturePath();
		if (!texturePath.getPath().startsWith("skins")) {
			return;
		}

		String url = ((ClientAsset.DownloadedTexture) texture).url();
		Identifier thiccIdentifier = texturePath.withPath(path -> "thicc/" + path);
		if (hasThiccArms(nativeImage)) {
			textureManager.register(thiccIdentifier, new DynamicTexture(thiccIdentifier::toString, nativeImage));
			cir.setReturnValue(new ClientAsset.DownloadedTexture(thiccIdentifier, url));
		} else {
			Identifier thinIdentifier = texturePath.withPath(path -> "thin/" + path);
			textureManager.register(thinIdentifier, new DynamicTexture(thinIdentifier::toString, nativeImage));
			cir.setReturnValue(new ClientAsset.DownloadedTexture(thinIdentifier, url));

		}
	}

	@Unique
	private static boolean hasThiccArms(NativeImage skin) {
		for (Rect2i armDiff : ARM_DIFF) {
			int xo = armDiff.getX();
			int yo = armDiff.getY();
			for (int x = armDiff.getWidth() - 1; x >= 0; x--) {
				for (int y = armDiff.getHeight() - 1; y >= 0; y--) {
					int rgba = skin.getPixel(xo + x, yo + y);
					if ((rgba & 0xFFFFFF) != 0 && (rgba >>> 24) != 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

}