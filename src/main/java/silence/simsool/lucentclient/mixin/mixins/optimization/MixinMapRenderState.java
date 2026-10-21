package silence.simsool.lucentclient.mixin.mixins.optimization;

import net.minecraft.client.renderer.state.MapRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import silence.simsool.lucentclient.features.fastrender.IMapRenderState;
import silence.simsool.lucentclient.features.fastrender.MapAtlasTexture;

@Mixin(MapRenderState.class)
public abstract class MixinMapRenderState implements IMapRenderState {

	@Unique
	private int lucent$atlasX;

	@Unique
	private int lucent$atlasY;

	@Unique
	private MapAtlasTexture lucent$atlasTexture;

	@Override
	public int lucent$getAtlasX() {
		return this.lucent$atlasX;
	}

	@Override
	public void lucent$setAtlasX(final int x) {
		this.lucent$atlasX = x;
	}

	@Override
	public int lucent$getAtlasY() {
		return this.lucent$atlasY;
	}

	@Override
	public void lucent$setAtlasY(final int y) {
		this.lucent$atlasY = y;
	}

	@Override
	public MapAtlasTexture lucent$getAtlasTexture() {
		return this.lucent$atlasTexture;
	}

	@Override
	public void lucent$setAtlasTexture(final MapAtlasTexture atlasTexture) {
		this.lucent$atlasTexture = atlasTexture;
	}

}