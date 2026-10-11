package silence.simsool.lucentclient.mods.impl.graphics.colorsaturation;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;

public class SaturationUniforms {

	private static final int BLOCK_SIZE = new Std140SizeCalculator()
			.putFloat()
			.putFloat()
			.putFloat()
			.putFloat()
			.get();

	private static float uploadedSaturation = Float.NaN;
	private static float uploadedContrast = Float.NaN;
	private static float uploadedBrightness = Float.NaN;
	private static float uploadedHue = Float.NaN;

	private static GpuBuffer buffer;

	public static GpuBuffer getBuffer() {
		if (buffer == null) {
			buffer = RenderSystem.getDevice().createBuffer(
					() -> "ColorSaturation_UBO",
					GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE,
					(long) BLOCK_SIZE
			);
		}
		return buffer;
	}

	public static void upload(float strength, float contrast, float brightness, float hue) {
		if (strength == uploadedSaturation
				&& contrast == uploadedContrast
				&& brightness == uploadedBrightness
				&& hue == uploadedHue) {
			return;
		}

		GpuBuffer buf = getBuffer();
		try (GpuBuffer.MappedView mapped = RenderSystem.getDevice().createCommandEncoder().mapBuffer(buf, false, true)) {
			mapped.data()
					.putFloat(strength)
					.putFloat(contrast)
					.putFloat(brightness)
					.putFloat(hue);
		}

		uploadedSaturation = strength;
		uploadedContrast = contrast;
		uploadedBrightness = brightness;
		uploadedHue = hue;
	}
}