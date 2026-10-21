package silence.simsool.lucentclient.mixin.mixins.optimization;

import static silence.simsool.lucentclient.features.fastrender.MapAtlasTexture.ATLAS_SIZE;
import static silence.simsool.lucentclient.features.fastrender.MapAtlasTexture.MAP_SIZE;

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
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(MapRenderer.class)
public abstract class MixinMapRenderer {

	@Shadow
	@Final
	private MapTextureManager mapTextureManager;

	@ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitCustomGeometry(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/SubmitNodeCollector$CustomGeometryRenderer;)V", ordinal = 0))
	private SubmitNodeCollector.CustomGeometryRenderer modifyTextureCoordinates(SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer, @Local(argsOnly = true) MapRenderState renderState, @Local(argsOnly = true) int light) {
		if (FastRenderMod.isEnabled() && FastRenderMod.MapAtlasGeneration) {
			final IMapRenderState mapRenderState = (IMapRenderState) renderState;
			if (mapRenderState.lucent$getAtlasTexture() != null && mapRenderState.lucent$getAtlasTexture().getTextureId().equals(renderState.texture)) {
				final float u1 = (float) mapRenderState.lucent$getAtlasX() / ATLAS_SIZE;
				final float u2 = (float) (mapRenderState.lucent$getAtlasX() + MAP_SIZE) / ATLAS_SIZE;
				final float v1 = (float) mapRenderState.lucent$getAtlasY() / ATLAS_SIZE;
				final float v2 = (float) (mapRenderState.lucent$getAtlasY() + MAP_SIZE) / ATLAS_SIZE;
				return (matrix, vertexConsumer) -> {
					vertexConsumer.addVertex(matrix, 0F, MAP_SIZE, -0.01F).setColor(-1).setUv(u1, v2).setLight(light);
					vertexConsumer.addVertex(matrix, MAP_SIZE, MAP_SIZE, -0.01F).setColor(-1).setUv(u2, v2).setLight(light);
					vertexConsumer.addVertex(matrix, MAP_SIZE, 0F, -0.01F).setColor(-1).setUv(u2, v1).setLight(light);
					vertexConsumer.addVertex(matrix, 0F, 0F, -0.01F).setColor(-1).setUv(u1, v1).setLight(light);
				};
			}
		}
		return customGeometryRenderer;
	}

	@Inject(method = "extractRenderState", at = @At("RETURN"))
	private void initAtlasParameters(MapId mapId, MapItemSavedData mapState, MapRenderState renderState, CallbackInfo ci) {
		if (!FastRenderMod.isEnabled() || !FastRenderMod.MapAtlasGeneration) {
			return;
		}

		final int packedLocation = ((IMapTextureManager) this.mapTextureManager).lucent$getAtlasMapping(mapId.id());
		if (packedLocation == -1) {
			return;
		}

		final IMapRenderState mapRenderState = (IMapRenderState) renderState;
		mapRenderState.lucent$setAtlasX(((packedLocation >> 8) & 0xFF) * MAP_SIZE);
		mapRenderState.lucent$setAtlasY((packedLocation & 0xFF) * MAP_SIZE);
		mapRenderState.lucent$setAtlasTexture(((IMapTextureManager) this.mapTextureManager).lucent$getMapAtlasTexture(packedLocation >> 16));
	}

}