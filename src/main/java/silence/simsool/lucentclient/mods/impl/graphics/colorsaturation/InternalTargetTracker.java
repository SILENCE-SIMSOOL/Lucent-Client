package silence.simsool.lucentclient.mods.impl.graphics.colorsaturation;

import org.joml.Vector4f;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.RenderTargetDescriptor;

public class InternalTargetTracker {

	private static RenderTargetDescriptor framebufferFactory;
	private static RenderTarget target;

	private static int prevWidth = -1;
	private static int prevHeight = -1;
	private static GpuFormat prevFormat = null;

	public static RenderTarget getTarget() {
		return target;
	}

	public static void updateSize(RenderTarget renderTarget) {
		int width = renderTarget.width;
		int height = renderTarget.height;
		GpuFormat format = renderTarget.getColorTexture().getFormat();

		if (width == prevWidth && height == prevHeight && format == prevFormat && target != null) {
			return;
		}

		framebufferFactory = new RenderTargetDescriptor(width, height, false, new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), format);
		prevFormat = format;

		free();
		target = framebufferFactory.allocate();
		prevWidth = width;
		prevHeight = height;
	}

	public static void free() {
		if (target != null) {
			if (framebufferFactory != null) {
				framebufferFactory.free(target);
			}
			target = null;
			prevWidth = -1;
			prevHeight = -1;
			prevFormat = null;
		}
	}
}