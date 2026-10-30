package silence.simsool.lucentclient.mods.impl.graphics.colorsaturation;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;

public class FullscreenTriangle {

	public static final int VERTEX_COUNT = 3;

	private static GpuBuffer vertexBuffer;

	public static GpuBuffer getVertexBuffer() {
		if (vertexBuffer == null) {
			ByteBufferBuilder storage = new ByteBufferBuilder(DefaultVertexFormat.POSITION.getVertexSize() * VERTEX_COUNT);
			BufferBuilder builder = new BufferBuilder(storage, PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION);

			builder.addVertex(-1.0f, -1.0f, 0.0f);
			builder.addVertex(3.0f, -1.0f, 0.0f);
			builder.addVertex(-1.0f, 3.0f, 0.0f);

			MeshData mesh = builder.buildOrThrow();
			try {
				vertexBuffer = RenderSystem.getDevice().createBuffer(
						() -> "ColorSaturation fullscreen triangle",
						GpuBuffer.USAGE_VERTEX,
						mesh.vertexBuffer()
				);
			} finally {
				mesh.close();
			}
		}
		return vertexBuffer;
	}

}