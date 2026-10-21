package silence.simsool.lucentclient.mixin.mixins.optimization;

import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import silence.simsool.lucentclient.features.fastrender.IMapRenderState;

@Mixin(MapRenderState.class)
public abstract class MixinMapRenderState implements IMapRenderState {

	@Unique
	private Identifier immediatelyFast$atlasTextureId;

	@Unique
	private int immediatelyFast$atlasX;

	@Unique
	private int immediatelyFast$atlasY;

	@Override
	public Identifier immediatelyFast$getAtlasTextureId() {
		return this.immediatelyFast$atlasTextureId;
	}

	@Override
	public void immediatelyFast$setAtlasTextureId(final Identifier identifier) {
		this.immediatelyFast$atlasTextureId = identifier;
	}

	@Override
	public int immediatelyFast$getAtlasX() {
		return this.immediatelyFast$atlasX;
	}

	@Override
	public void immediatelyFast$setAtlasX(final int x) {
		this.immediatelyFast$atlasX = x;
	}

	@Override
	public int immediatelyFast$getAtlasY() {
		return this.immediatelyFast$atlasY;
	}

	@Override
	public void immediatelyFast$setAtlasY(final int y) {
		this.immediatelyFast$atlasY = y;
	}

}