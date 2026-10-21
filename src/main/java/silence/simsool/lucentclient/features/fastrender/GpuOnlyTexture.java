package silence.simsool.lucentclient.features.fastrender;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.textures.AddressMode;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTexture;
import java.nio.file.Path;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.Dumpable;
import net.minecraft.resources.Identifier;

public class GpuOnlyTexture extends AbstractTexture implements Dumpable {

	public GpuOnlyTexture(final String label, final int width, final int height) {
		this.texture = RenderSystem.getDevice().createTexture(label, GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_TEXTURE_BINDING, GpuFormat.RGBA8_UNORM, width, height, 1, 1);
		this.textureView = RenderSystem.getDevice().createTextureView(this.texture);
		this.sampler = RenderSystem.getSamplerCache().getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.NEAREST, FilterMode.NEAREST, false);
	}

	@Override
	public void dumpContents(final Identifier selfId, final Path dir) {
		TextureUtil.writeAsPNG(dir, selfId.toDebugFileName(), this.getTexture(), 0, argb -> argb);
	}

}