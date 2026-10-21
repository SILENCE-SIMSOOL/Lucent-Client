package silence.simsool.lucentclient.features.fastrender;

import net.minecraft.resources.Identifier;

public interface IMapRenderState {

	Identifier immediatelyFast$getAtlasTextureId();

	void immediatelyFast$setAtlasTextureId(final Identifier identifier);

	int immediatelyFast$getAtlasX();

	void immediatelyFast$setAtlasX(final int x);

	int immediatelyFast$getAtlasY();

	void immediatelyFast$setAtlasY(final int y);

}