package silence.simsool.lucentclient.features.fastrender;

import java.util.Collection;

public interface IMapTextureManager {

	MapAtlas immediatelyFast$getAtlas(final int id);

	int immediatelyFast$getAtlasLocation(final int mapId);

	Collection<MapAtlas> immediatelyFast$getAtlases();

}