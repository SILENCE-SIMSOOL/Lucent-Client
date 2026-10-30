package silence.simsool.lucentclient.mods.impl.graphics.colorsaturation;

import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;

public class SaturationUniforms {

	private static final int BLOCK_SIZE = new Std140SizeCalculator()
			.putFloat()
			.putFloat()
			.putFloat()
			.putFloat()
			.get();

	private static GpuBuffer buffer;

	private static float uploadedSaturation = Float.NaN;
	private static float uploadedContrast = Float.NaN;
	private static float uploadedBrightness = Float.NaN;
	private static float uploadedHue = Float.NaN;

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
		try (GpuBufferSlice.MappedView mapped = buf.map(false, true)) {
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