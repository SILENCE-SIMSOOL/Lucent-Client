package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.resources.MapTextureManager;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silence.simsool.lucentclient.features.fastrender.IMapRenderState;
import silence.simsool.lucentclient.features.fastrender.IMapTextureManager;
import silence.simsool.lucentclient.features.fastrender.MapAtlas;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(MapRenderer.class)
public abstract class MixinMapRenderer {

	@Shadow
	@Final
	private MapTextureManager mapTextureManager;

	@ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitCustomGeometry(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/SubmitNodeCollector$CustomGeometryRenderer;)V", ordinal = 0))
	private SubmitNodeCollector.CustomGeometryRenderer modifyTextureCoordinates(final SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer, @Local(name = "mapRenderState", argsOnly = true) final MapRenderState mapRenderState, @Local(name = "lightCoords", argsOnly = true) final int lightCoords) {
		if (FastRenderMod.isEnabled() && FastRenderMod.MapAtlasGeneration) {
			final IMapRenderState immediatelyFast$mapRenderState = (IMapRenderState) mapRenderState;
			if (immediatelyFast$mapRenderState.immediatelyFast$getAtlasTextureId() != null && immediatelyFast$mapRenderState.immediatelyFast$getAtlasTextureId().equals(mapRenderState.texture)) {
				final float u0 = (float) immediatelyFast$mapRenderState.immediatelyFast$getAtlasX() / MapAtlas.ATLAS_SIZE;
				final float u1 = (float) (immediatelyFast$mapRenderState.immediatelyFast$getAtlasX() + MapAtlas.MAP_SIZE) / MapAtlas.ATLAS_SIZE;
				final float v0 = (float) immediatelyFast$mapRenderState.immediatelyFast$getAtlasY() / MapAtlas.ATLAS_SIZE;
				final float v1 = (float) (immediatelyFast$mapRenderState.immediatelyFast$getAtlasY() + MapAtlas.MAP_SIZE) / MapAtlas.ATLAS_SIZE;
				return (matrix, vertexConsumer) -> {
					vertexConsumer.addVertex(matrix, 0F, MapAtlas.MAP_SIZE, -0.01F).setColor(-1).setUv(u0, v1).setLight(lightCoords);
					vertexConsumer.addVertex(matrix, MapAtlas.MAP_SIZE, MapAtlas.MAP_SIZE, -0.01F).setColor(-1).setUv(u1, v1).setLight(lightCoords);
					vertexConsumer.addVertex(matrix, MapAtlas.MAP_SIZE, 0F, -0.01F).setColor(-1).setUv(u1, v0).setLight(lightCoords);
					vertexConsumer.addVertex(matrix, 0F, 0F, -0.01F).setColor(-1).setUv(u0, v0).setLight(lightCoords);
				};
			}
		}
		return customGeometryRenderer;
	}

	@Inject(method = "extractRenderState", at = @At("RETURN"))
	private void setAtlasParameters(final MapId mapId, final MapItemSavedData mapData, final MapRenderState mapRenderState, final CallbackInfo ci) {
		final IMapRenderState immediatelyFast$mapRenderState = (IMapRenderState) mapRenderState;
		if (FastRenderMod.isEnabled() && FastRenderMod.MapAtlasGeneration) {
			final int location = ((IMapTextureManager) this.mapTextureManager).immediatelyFast$getAtlasLocation(mapId.id());
			if (location != -1) {
				immediatelyFast$mapRenderState.immediatelyFast$setAtlasTextureId(((IMapTextureManager) this.mapTextureManager).immediatelyFast$getAtlas(MapAtlas.getAtlasIdFromLocation(location)).getTextureId());
				immediatelyFast$mapRenderState.immediatelyFast$setAtlasX(MapAtlas.getAtlasXFromLocation(location) * MapAtlas.MAP_SIZE);
				immediatelyFast$mapRenderState.immediatelyFast$setAtlasY(MapAtlas.getAtlasYFromLocation(location) * MapAtlas.MAP_SIZE);
				mapRenderState.texture = immediatelyFast$mapRenderState.immediatelyFast$getAtlasTextureId();
				return;
			}
		}
		immediatelyFast$mapRenderState.immediatelyFast$setAtlasTextureId(null);
		immediatelyFast$mapRenderState.immediatelyFast$setAtlasX(0);
		immediatelyFast$mapRenderState.immediatelyFast$setAtlasY(0);
	}

}