package silence.simsool.lucentclient.features.fastrender;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectSortedMaps;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import java.util.Arrays;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

public class BatchableBufferSource extends MultiBufferSource.BufferSource implements AutoCloseable {

	private static final ByteBufferBuilder FALLBACK_BUFFER = new ByteBufferBuilder(0);

	protected final Map<RenderType, ReferenceSet<BufferBuilder>> dynamicBuffers = IrisCompat.IRIS_LOADED ? new Object2ObjectLinkedOpenHashMap<>() : new Reference2ObjectLinkedOpenHashMap<>();
	protected final Set<RenderType> activeRenderTypes = IrisCompat.IRIS_LOADED ? new ObjectLinkedOpenHashSet<>() : new ReferenceLinkedOpenHashSet<>();

	protected boolean drawDynamicBuffersFirst = false;

	public BatchableBufferSource() {
		this(Object2ObjectSortedMaps.emptyMap());
	}

	public BatchableBufferSource(final SequencedMap<RenderType, ByteBufferBuilder> fixedBuffers) {
		this(FALLBACK_BUFFER, fixedBuffers);
	}

	public BatchableBufferSource(final ByteBufferBuilder sharedBuffer, final SequencedMap<RenderType, ByteBufferBuilder> fixedBuffers) {
		super(sharedBuffer, fixedBuffers);
	}

	@Override
	public VertexConsumer getBuffer(final RenderType renderType) {
		if (!this.drawDynamicBuffersFirst) {
			if (this.lastSharedType != null && this.lastSharedType != renderType && !this.fixedBuffers.containsKey(this.lastSharedType)) {
				this.drawDynamicBuffersFirst = true;
			}
		}

		if (IrisCompat.IRIS_LOADED && IrisCompat.skipExtension != null) {
			IrisCompat.skipExtension.set(!IrisCompat.isRenderingLevel.getAsBoolean());
		}

		final BufferBuilder bufferBuilder;
		final boolean hasBufferForRenderType = renderType.canConsolidateConsecutiveGeometry() && this.dynamicBuffers.containsKey(renderType);
		if (!renderType.canConsolidateConsecutiveGeometry()) {
			bufferBuilder = new BufferBuilder(this.getNextByteBufferBuilder(), renderType.mode(), renderType.format());
			this.lastSharedType = renderType;
		} else if (hasBufferForRenderType) {
			bufferBuilder = this.dynamicBuffers.get(renderType).iterator().next();
		} else if (this.fixedBuffers.containsKey(renderType)) {
			bufferBuilder = new BufferBuilder(this.fixedBuffers.get(renderType), renderType.mode(), renderType.format());
		} else {
			bufferBuilder = new BufferBuilder(this.getNextByteBufferBuilder(), renderType.mode(), renderType.format());
			this.lastSharedType = renderType;
		}

		if (IrisCompat.IRIS_LOADED && IrisCompat.skipExtension != null) {
			IrisCompat.skipExtension.set(false);
		}

		if (hasBufferForRenderType) {
			if (renderType.name.contains("immediatelyfast:renderlast") && this.activeRenderTypes.contains(renderType)) {
				this.activeRenderTypes.remove(renderType);
				this.activeRenderTypes.add(renderType);
			}
		} else {
			this.dynamicBuffers.computeIfAbsent(renderType, k -> new ReferenceLinkedOpenHashSet<>()).add(bufferBuilder);
			this.activeRenderTypes.add(renderType);
		}

		return bufferBuilder;
	}

	@Override
	public void endLastBatch() {
		this.lastSharedType = null;
		this.drawDynamicBuffersFirst = false;

		int sortedRenderTypesLength = 0;
		final RenderType[] sortedRenderTypes = new RenderType[this.activeRenderTypes.size()];
		for (RenderType renderType : this.activeRenderTypes) {
			if (!this.fixedBuffers.containsKey(renderType)) {
				sortedRenderTypes[sortedRenderTypesLength++] = renderType;
			}
		}
		if (sortedRenderTypesLength == 0) {
			return;
		}

		Arrays.sort(sortedRenderTypes, (t1, t2) -> Integer.compare(this.getRenderTypeOrder(t1), this.getRenderTypeOrder(t2)));
		for (int i = 0; i < sortedRenderTypesLength; i++) {
			this.endBatch(sortedRenderTypes[i]);
		}
	}

	@Override
	public void endBatch() {
		if (this.activeRenderTypes.isEmpty()) {
			this.close();
			return;
		}

		this.endLastBatch();
		for (RenderType renderType : this.fixedBuffers.keySet()) {
			this.endBatch(renderType);
		}
	}

	@Override
	public void endBatch(final RenderType renderType) {
		if (this.drawDynamicBuffersFirst) {
			this.endLastBatch();
		}

		this.drawDirect(renderType);
	}

	@Override
	public void close() {
		this.lastSharedType = null;
		this.drawDynamicBuffersFirst = false;

		for (Set<BufferBuilder> buffers : this.dynamicBuffers.values()) {
			for (BufferBuilder bufferBuilder : buffers) {
				final MeshData meshData = bufferBuilder.build();
				if (meshData != null) {
					meshData.close();
				}
				ByteBufferBuilderPool.returnBufferBuilderSafe(bufferBuilder.buffer);
			}
		}

		this.activeRenderTypes.clear();
		this.dynamicBuffers.clear();
	}

	public void drawDirect(final RenderType renderType) {
		if (IrisCompat.IRIS_LOADED && IrisCompat.renderWithExtendedVertexFormat != null && !IrisCompat.isRenderingLevel.getAsBoolean()) {
			IrisCompat.renderWithExtendedVertexFormat.accept(false);
		}

		this.activeRenderTypes.remove(renderType);
		final Set<BufferBuilder> buffers = this.dynamicBuffers.remove(renderType);
		if (buffers != null) {
			for (BufferBuilder bufferBuilder : buffers) {
				final ByteBufferBuilder prevBufferBuilder = this.sharedBuffer;
				this.sharedBuffer = bufferBuilder.buffer;
				this.endBatch(renderType, bufferBuilder);
				this.sharedBuffer = prevBufferBuilder;
				ByteBufferBuilderPool.returnBufferBuilderSafe(bufferBuilder.buffer);
			}
		}
		if (this.lastSharedType == renderType) {
			this.lastSharedType = null;
		}

		if (IrisCompat.IRIS_LOADED && IrisCompat.renderWithExtendedVertexFormat != null && !IrisCompat.isRenderingLevel.getAsBoolean()) {
			IrisCompat.renderWithExtendedVertexFormat.accept(true);
		}
	}

	public boolean hasActiveRenderTypes() {
		return !this.activeRenderTypes.isEmpty();
	}

	protected int getRenderTypeOrder(final RenderType renderType) {
		if (renderType == null) {
			return Integer.MAX_VALUE;
		}

		int order = 0;
		final RenderSetup.TextureBinding textureBinding = renderType.state.textures.get("Sampler0");
		if (textureBinding != null) {
			final Identifier textureId = textureBinding.location();
			if (renderType.name.startsWith("text") || renderType.name.startsWith("neoforge_text")) {
				if (textureId.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
					order = 2;
				} else {
					order = 1;
				}
			}
		}

		if (!renderType.sortOnUpload()) {
			return order;
		} else {
			return 100_000_000 + order;
		}
	}

	private ByteBufferBuilder getNextByteBufferBuilder() {
		if (this.sharedBuffer != FALLBACK_BUFFER && this.lastSharedType == null && this.sharedBuffer.pointer != 0L) {
			return this.sharedBuffer;
		} else {
			return ByteBufferBuilderPool.borrowBufferBuilder();
		}
	}

}