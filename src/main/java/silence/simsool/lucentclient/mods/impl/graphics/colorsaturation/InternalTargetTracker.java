package silence.simsool.lucentclient.mods.impl.graphics.colorsaturation;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.RenderTargetDescriptor;

public class InternalTargetTracker {

	private static RenderTargetDescriptor framebufferFactory;
	private static RenderTarget target;
	private static int prevWidth = -1;
	private static int prevHeight = -1;

	public static RenderTarget getTarget() {
		return target;
	}

	public static void updateSize(RenderTarget renderTarget) {
		int width = renderTarget.width;
		int height = renderTarget.height;

		if (width == prevWidth && height == prevHeight && target != null) {
			return;
		}

		if (framebufferFactory == null || framebufferFactory.width() != width || framebufferFactory.height() != height) {
			framebufferFactory = new RenderTargetDescriptor(width, height, false, 0);
		}

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
		}
	}
}