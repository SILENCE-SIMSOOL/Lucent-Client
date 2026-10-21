package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.MapTextureManager;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silence.simsool.lucentclient.features.fastrender.IMapTextureManager;
import silence.simsool.lucentclient.features.fastrender.MapAtlas;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(targets = "net.minecraft.client.resources.MapTextureManager$MapInstance")
public abstract class MixinMapTextureManager_MapInstance {

	@Shadow
	@Final
	private DynamicTexture texture;

	@Unique
	private AbstractTexture immediatelyFast$atlasTexture;

	@Unique
	private int immediatelyFast$atlasX;

	@Unique
	private int immediatelyFast$atlasY;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void getAtlasParameters(final MapTextureManager mapTextureManager, final int id, final MapItemSavedData data, final CallbackInfo ci) {
		if (FastRenderMod.isEnabled() && FastRenderMod.MapAtlasGeneration) {
			final int location = ((IMapTextureManager) mapTextureManager).immediatelyFast$getAtlasLocation(id);
			if (location != -1) {
				this.immediatelyFast$atlasTexture = ((IMapTextureManager) mapTextureManager).immediatelyFast$getAtlas(MapAtlas.getAtlasIdFromLocation(location)).getTexture();
				this.immediatelyFast$atlasX = MapAtlas.getAtlasXFromLocation(location) * MapAtlas.MAP_SIZE;
				this.immediatelyFast$atlasY = MapAtlas.getAtlasYFromLocation(location) * MapAtlas.MAP_SIZE;
			}
		}
	}

	@Inject(method = "updateTextureIfNeeded", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/DynamicTexture;upload()V", shift = At.Shift.AFTER))
	private void updateAtlasTexture(final CallbackInfo ci) {
		if (FastRenderMod.isEnabled() && FastRenderMod.MapAtlasGeneration && this.immediatelyFast$atlasTexture != null) {
			RenderSystem.getDevice().createCommandEncoder().writeToTexture(this.immediatelyFast$atlasTexture.getTexture(), this.texture.getPixels(), 0, 0, this.immediatelyFast$atlasX, this.immediatelyFast$atlasY);
		}
	}

}