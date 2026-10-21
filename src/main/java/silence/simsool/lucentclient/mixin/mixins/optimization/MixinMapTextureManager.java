package silence.simsool.lucentclient.mixin.mixins.optimization;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Collection;
import net.minecraft.client.resources.MapTextureManager;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import silence.simsool.lucentclient.features.fastrender.IMapTextureManager;
import silence.simsool.lucentclient.features.fastrender.MapAtlasTexture;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(MapTextureManager.class)
public abstract class MixinMapTextureManager implements IMapTextureManager {

	@Unique
	private final Int2ObjectMap<MapAtlasTexture> lucent$mapAtlasTextures = new Int2ObjectOpenHashMap<>();

	@Unique
	private final Int2IntMap lucent$mapIdToAtlasMapping = new Int2IntOpenHashMap();

	@Inject(method = "resetData", at = @At("RETURN"))
	private void clearMapAtlas(final CallbackInfo ci) {
		for (MapAtlasTexture texture : this.lucent$mapAtlasTextures.values()) {
			texture.close();
		}

		this.lucent$mapAtlasTextures.clear();
		this.lucent$mapIdToAtlasMapping.clear();
	}

	@Inject(method = "getOrCreateMapInstance", at = @At("HEAD"))
	private void createMapAtlasTexture(MapId mapId, MapItemSavedData data, CallbackInfoReturnable<MapTextureManager.MapInstance> cir) {
		if (!FastRenderMod.isEnabled() || !FastRenderMod.MapAtlasGeneration) {
			return;
		}

		this.lucent$mapIdToAtlasMapping.computeIfAbsent(mapId.id(), k -> {
			for (MapAtlasTexture atlasTexture : this.lucent$mapAtlasTextures.values()) {
				final int location = atlasTexture.getNextMapLocation();
				if (location != -1) {
					return location;
				}
			}

			final MapAtlasTexture atlasTexture = new MapAtlasTexture(this.lucent$mapAtlasTextures.size());
			this.lucent$mapAtlasTextures.put(atlasTexture.getId(), atlasTexture);
			return atlasTexture.getNextMapLocation();
		});
	}

	@Override
	public MapAtlasTexture lucent$getMapAtlasTexture(final int id) {
		return this.lucent$mapAtlasTextures.get(id);
	}

	@Override
	public int lucent$getAtlasMapping(final int mapId) {
		return this.lucent$mapIdToAtlasMapping.getOrDefault(mapId, -1);
	}

	@Override
	public Collection<MapAtlasTexture> lucent$getAllMapAtlasTextures() {
		return this.lucent$mapAtlasTextures.values();
	}

}