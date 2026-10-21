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
import silence.simsool.lucentclient.features.fastrender.MapAtlas;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(MapTextureManager.class)
public abstract class MixinMapTextureManager implements IMapTextureManager {

	@Unique
	private final Int2ObjectMap<MapAtlas> immediatelyFast$atlases = new Int2ObjectOpenHashMap<>();

	@Unique
	private final Int2IntMap immediatelyFast$mapIdToAtlasLocation = new Int2IntOpenHashMap();

	@Inject(method = "resetData", at = @At("RETURN"))
	private void resetAtlases(final CallbackInfo ci) {
		for (MapAtlas atlas : this.immediatelyFast$atlases.values()) {
			atlas.close();
		}
		this.immediatelyFast$atlases.clear();
		this.immediatelyFast$mapIdToAtlasLocation.clear();
	}

	@Inject(method = "getOrCreateMapInstance", at = @At("HEAD"))
	private void ensureHasAtlasLocation(final MapId id, final MapItemSavedData data, final CallbackInfoReturnable<?> cir) {
		if (FastRenderMod.isEnabled() && FastRenderMod.MapAtlasGeneration) {
			this.immediatelyFast$mapIdToAtlasLocation.computeIfAbsent(id.id(), k -> {
				for (MapAtlas atlas : this.immediatelyFast$atlases.values()) {
					final int location = atlas.getNextLocation();
					if (location != -1) {
						return location;
					}
				}

				final MapAtlas atlas = new MapAtlas(this.immediatelyFast$atlases.size());
				this.immediatelyFast$atlases.put(atlas.getId(), atlas);
				return atlas.getNextLocation();
			});
		}
	}

	@Override
	public MapAtlas immediatelyFast$getAtlas(final int id) {
		return this.immediatelyFast$atlases.get(id);
	}

	@Override
	public int immediatelyFast$getAtlasLocation(final int mapId) {
		return this.immediatelyFast$mapIdToAtlasLocation.getOrDefault(mapId, -1);
	}

	@Override
	public Collection<MapAtlas> immediatelyFast$getAtlases() {
		return this.immediatelyFast$atlases.values();
	}

}