package silence.simsool.lucentclient.features.fastrender;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceList;

public class ByteBufferBuilderPool {

	private static final ReferenceList<Entry> FREE = new ReferenceArrayList<>();
	private static final ReferenceList<Entry> IN_USE = new ReferenceArrayList<>();
	private static final Reference2ObjectMap<ByteBufferBuilder, Entry> BUFFER_BUILDER_MAPPING = new Reference2ObjectOpenHashMap<>();

	private ByteBufferBuilderPool() {}

	public static ByteBufferBuilder borrowBufferBuilder() {
		RenderSystem.assertOnRenderThread();
		Entry entry;
		if (FREE.isEmpty()) {
			entry = new Entry(new ByteBufferBuilder(256));
		} else {
			entry = FREE.removeFirst();
			if (entry.bufferBuilder.pointer == 0L) {
				BUFFER_BUILDER_MAPPING.remove(entry.bufferBuilder);
				entry = new Entry(new ByteBufferBuilder(256));
			}
		}
		IN_USE.add(entry);
		BUFFER_BUILDER_MAPPING.put(entry.bufferBuilder, entry);
		entry.onBorrow();
		return entry.bufferBuilder;
	}

	public static void returnBufferBuilderSafe(final ByteBufferBuilder bufferBuilder) {
		RenderSystem.assertOnRenderThread();
		final Entry entry = BUFFER_BUILDER_MAPPING.get(bufferBuilder);
		if (!IN_USE.remove(entry)) {
			return;
		}
		entry.onReturn();
		FREE.addFirst(entry);
	}

	public static void onEndFrame() {
		if (!IN_USE.isEmpty()) {
			IN_USE.removeIf(entry -> entry.inUseOverMultipleFrames);
			for (Entry entry : IN_USE) {
				entry.inUseOverMultipleFrames = true;
			}
		}

		FREE.removeIf(entry -> {
			if (entry.shouldBeClosed()) {
				entry.bufferBuilder.close();
				BUFFER_BUILDER_MAPPING.remove(entry.bufferBuilder);
				return true;
			}
			return false;
		});
	}

	private static class Entry {

		private final ByteBufferBuilder bufferBuilder;
		private long lastAccessTime;
		private boolean inUseOverMultipleFrames;

		public Entry(final ByteBufferBuilder bufferBuilder) {
			this.bufferBuilder = bufferBuilder;
			this.lastAccessTime = System.currentTimeMillis();
		}

		public boolean shouldBeClosed() {
			return System.currentTimeMillis() - this.lastAccessTime > 60 * 1000;
		}

		public void onBorrow() {
			this.lastAccessTime = System.currentTimeMillis();
		}

		public void onReturn() {
			this.bufferBuilder.discard();
			this.inUseOverMultipleFrames = false;
		}

	}

}