package silence.simsool.lucentclient.mixin.mixins.optimization;

import static silence.simsool.lucentclient.features.fastrender.MapAtlasTexture.MAP_SIZE;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import java.lang.reflect.Field;
import java.util.function.Supplier;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.MapTextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silence.simsool.lucentclient.features.fastrender.IMapTextureManager;
import silence.simsool.lucentclient.features.fastrender.MapAtlasTexture;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(MapTextureManager.MapInstance.class)
public abstract class MixinMapTextureManager_MapInstance {

	@Shadow
	private MapItemSavedData data;

	@Mutable
	@Shadow
	@Final
	private DynamicTexture texture;

	@Shadow
	private boolean requiresUpload;

	@Shadow
	@Final
	@Mutable
	Identifier location;

	@Unique
	private static final DynamicTexture DUMMY_TEXTURE;

	static {
		DynamicTexture dummy = null;
		try {
			Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
			dummy = (DynamicTexture) unsafe.allocateInstance(DynamicTexture.class);
		} catch (Throwable ignored) {}
		DUMMY_TEXTURE = dummy;
	}

	@Unique
	private int lucent$atlasX;

	@Unique
	private int lucent$atlasY;

	@Unique
	private MapAtlasTexture lucent$atlasTexture;

	@Redirect(method = "<init>", at = @At(value = "NEW", target = "(Ljava/util/function/Supplier;IIZ)Lnet/minecraft/client/renderer/texture/DynamicTexture;"))
	private DynamicTexture initAtlasParametersAndDontAllocateTexture(Supplier<String> label, int width, int height, boolean useCalloc, @Local(argsOnly = true) MapTextureManager mapTextureManager, @Local(argsOnly = true) int id) {
		if (FastRenderMod.isEnabled() && FastRenderMod.MapAtlasGeneration) {
			final int packedLocation = ((IMapTextureManager) mapTextureManager).lucent$getAtlasMapping(id);
			if (packedLocation != -1) {
				this.lucent$atlasX = ((packedLocation >> 8) & 0xFF) * MAP_SIZE;
				this.lucent$atlasY = (packedLocation & 0xFF) * MAP_SIZE;
				this.lucent$atlasTexture = ((IMapTextureManager) mapTextureManager).lucent$getMapAtlasTexture(packedLocation >> 16);
				if (this.lucent$atlasTexture != null && DUMMY_TEXTURE != null) {
					return DUMMY_TEXTURE;
				}
			}
		}

		return new DynamicTexture(label, width, height, useCalloc);
	}

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;register(Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/renderer/texture/AbstractTexture;)V"))
	private void getAtlasTextureIdentifier(TextureManager instance, Identifier path, AbstractTexture texture) {
		if (this.lucent$atlasTexture != null) {
			this.texture = null;
			this.location = this.lucent$atlasTexture.getTextureId();
		} else {
			instance.register(path, texture);
		}
	}

	@Inject(method = "updateTextureIfNeeded", at = @At("HEAD"), cancellable = true)
	private void updateAtlasTexture(CallbackInfo ci) {
		if (this.requiresUpload && this.lucent$atlasTexture != null) {
			ci.cancel();
			final DynamicTexture atlasTexture = this.lucent$atlasTexture.getTexture();
			final NativeImage atlasImage = atlasTexture.getPixels();
			if (atlasImage == null) {
				return;
			}

			for (int x = 0; x < MAP_SIZE; x++) {
				for (int y = 0; y < MAP_SIZE; y++) {
					final int i = x + y * MAP_SIZE;
					atlasImage.setPixel(this.lucent$atlasX + x, this.lucent$atlasY + y, MapColor.getColorFromPackedId(this.data.colors[i]));
				}
			}
			RenderSystem.getDevice().createCommandEncoder().writeToTexture(atlasTexture.getTexture(), atlasImage, 0, 0, this.lucent$atlasX, this.lucent$atlasY, MAP_SIZE, MAP_SIZE, this.lucent$atlasX, this.lucent$atlasY);
			this.requiresUpload = false;
		}
	}

	@Inject(method = "close", at = @At("HEAD"), cancellable = true)
	private void dontCloseDummyTexture(CallbackInfo ci) {
		if (this.lucent$atlasTexture != null) {
			ci.cancel();
		}
	}

}