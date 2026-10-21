package silence.simsool.lucentclient.features.fastrender;

import java.util.Collection;

public interface IMapTextureManager {

	MapAtlasTexture lucent$getMapAtlasTexture(final int id);

	int lucent$getAtlasMapping(final int mapId);

	Collection<MapAtlasTexture> lucent$getAllMapAtlasTextures();

}