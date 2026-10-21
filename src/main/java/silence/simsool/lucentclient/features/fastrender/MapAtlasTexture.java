package silence.simsool.lucentclient.features.fastrender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public class MapAtlasTexture implements AutoCloseable {

	public static final int ATLAS_SIZE = 2048;
	public static final int MAP_SIZE = 128;
	public static final int MAPS_PER_ATLAS = (ATLAS_SIZE / MAP_SIZE) * (ATLAS_SIZE / MAP_SIZE);

	private final int id;
	private final Identifier textureId;
	private final DynamicTexture texture;
	private int mapCount;

	public MapAtlasTexture(final int id) {
		this.id = id;
		this.textureId = Identifier.fromNamespaceAndPath("lucentclient", "map_atlas/" + id);
		this.texture = new DynamicTexture("LucentClient Map Atlas", ATLAS_SIZE, ATLAS_SIZE, true);
		Minecraft.getInstance().getTextureManager().register(this.textureId, this.texture);
	}

	public int getNextMapLocation() {
		if (this.mapCount >= MAPS_PER_ATLAS) {
			return -1;
		}

		final byte atlasX = (byte) (this.mapCount % (ATLAS_SIZE / MAP_SIZE));
		final byte atlasY = (byte) (this.mapCount / (ATLAS_SIZE / MAP_SIZE));
		this.mapCount++;

		return (this.id << 16) | (atlasX << 8) | atlasY;
	}

	public int getId() {
		return this.id;
	}

	public Identifier getTextureId() {
		return this.textureId;
	}

	public DynamicTexture getTexture() {
		return this.texture;
	}

	public int getMapCount() {
		return this.mapCount;
	}

	@Override
	public void close() {
		Minecraft.getInstance().getTextureManager().release(this.textureId);
	}

}