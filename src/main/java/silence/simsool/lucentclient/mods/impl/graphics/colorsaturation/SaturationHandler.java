package silence.simsool.lucentclient.mods.impl.graphics.colorsaturation;

import java.util.Optional;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.textures.FilterMode;

import net.minecraft.resources.Identifier;
import silence.simsool.lucentclient.mods.impl.graphics.ColorSaturationMod;

public class SaturationHandler {

	private static RenderPipeline saturationPipeline;
	private static RenderPipeline blitPipeline;

	private static RenderPipeline createPipeline(String name, String fragmentShader, boolean hasUniforms) {
		RenderPipeline.Builder builder = RenderPipeline.builder()
				.withLocation(Identifier.parse("colorsaturation:" + name))
				.withVertexShader(Identifier.parse("colorsaturation:core/fullscreen_triangle"))
				.withFragmentShader(Identifier.parse("colorsaturation:" + fragmentShader))
				.withVertexBinding(0, DefaultVertexFormat.POSITION)
				.withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
				.withDepthStencilState(Optional.empty())
				.withColorTargetState(ColorTargetState.DEFAULT);

		BindGroupLayout.Builder bindGroupLayout = BindGroupLayout.builder()
				.withUniform("DiffuseSampler", UniformType.COMBINED_IMAGE_SAMPLER);

		if (hasUniforms) {
			bindGroupLayout.withUniform("SaturationConfig", UniformType.UNIFORM_BUFFER);
		}

		builder.withBindGroupLayout(bindGroupLayout.build());
		return builder.build();
	}

	private static RenderPipeline getSaturationPipeline() {
		if (saturationPipeline == null) {
			saturationPipeline = createPipeline("saturation_pipeline", "post/color_saturation", true);
		}
		return saturationPipeline;
	}

	private static RenderPipeline getBlitPipeline() {
		if (blitPipeline == null) {
			blitPipeline = createPipeline("blit_pipeline", "post/blit", false);
		}
		return blitPipeline;
	}

	public static void free() {
		InternalTargetTracker.free();
	}

	public static void render(RenderTarget renderTarget, CrossFrameResourcePool resourcePool) {
		if (!ColorSaturationMod.isActive()) {
			InternalTargetTracker.free();
			return;
		}

		InternalTargetTracker.updateSize(renderTarget);
		SaturationUniforms.upload(
				ColorSaturationMod.Strength,
				ColorSaturationMod.Contrast,
				ColorSaturationMod.Brightness,
				ColorSaturationMod.Hue
		);

		RenderTarget tempTarget = InternalTargetTracker.getTarget();
		if (tempTarget == null) return;

		drawPass("ColorSaturation/Saturation", tempTarget, renderTarget, getSaturationPipeline(), true);
		drawPass("ColorSaturation/Blit", renderTarget, tempTarget, getBlitPipeline(), false);
	}

	private static void drawPass(
			String label,
			RenderTarget output,
			RenderTarget input,
			RenderPipeline pipeline,
			boolean hasUniforms
	) {
		CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
		GpuBuffer vertexBuffer = FullscreenTriangle.getVertexBuffer();

		try (RenderPass renderPass = commandEncoder.createRenderPass(
				() -> label,
				output.getColorTextureView(),
				Optional.empty()
		)) {
			renderPass.setPipeline(RenderSystem.getCompiledPipeline(pipeline));
			renderPass.setVertexBuffer(0, vertexBuffer.slice());
			renderPass.setUniform(
					"DiffuseSampler",
					input.getColorTextureView(),
					RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST)
			);
			if (hasUniforms) {
				renderPass.setUniform("SaturationConfig", SaturationUniforms.getBuffer());
			}
			renderPass.draw(FullscreenTriangle.VERTEX_COUNT, 1, 0, 0);
		}
	}
}