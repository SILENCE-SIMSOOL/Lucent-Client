package silence.simsool.lucentclient.features.fastrender;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.render.DynamicAtlasAllocator;
import net.minecraft.client.gui.render.GuiItemAtlas;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.util.Mth;

public class AnimatedItemAtlas extends GuiItemAtlas {

	private static final int MINIMUM_TEXTURE_SIZE = 128;

	private int lastUsedSlotCount;

	public static int computeTextureSizeFor(final int slotTextureSize, final int requiredSlotCount) {
		final int preferredSlotCount = requiredSlotCount + requiredSlotCount / 2;
		final int atlasSize = Mth.smallestSquareSide(preferredSlotCount);
		return Math.clamp(Mth.smallestEncompassingPowerOfTwo(atlasSize * slotTextureSize), MINIMUM_TEXTURE_SIZE, RenderSystem.getDevice().getDeviceInfo().limits().maxTextureSizeForFormat(GpuFormat.RGBA8_UNORM));
	}

	public AnimatedItemAtlas(final FeatureRenderDispatcher featureRenderDispatcher, final int textureSize, final int slotTextureSize) {
		super(featureRenderDispatcher, textureSize, slotTextureSize);
	}

	@Override
	public SlotView getOrUpdate(final TrackingItemStackRenderState item) {
		if (!item.isAnimated()) {
			throw new IllegalArgumentException("Item is not animated");
		}
		return super.getOrUpdate(item);
	}

	@Override
	public void endFrame() {
		this.lastUsedSlotCount = this.allocator.usedSlotByKey.size();
		if (this.lastUsedSlotCount > 0) {
			RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.texture, GuiRenderer.CLEAR_COLOR, this.depthTexture, 0D);
			for (DynamicAtlasAllocator.Slot slot : this.allocator.usedSlotByKey.values()) {
				slot.fresh = true;
			}
		}
		super.endFrame();
	}

	public int getLastUsedSlotCount() {
		return this.lastUsedSlotCount;
	}

}