package silence.simsool.lucentclient.mods.impl.utility.packmanager.utils;

import java.io.InputStream;
import java.util.Set;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.resources.IoSupplier;

public class FilteredPackResources implements PackResources {
	private final PackResources delegate;
	private static final String FONT_PATH = "font/default.json";

	public FilteredPackResources(PackResources delegate) {
		this.delegate = delegate;
	}

	@Override
	public IoSupplier<InputStream> getRootResource(String... paths) {
		return delegate.getRootResource(paths);
	}

	@Override
	public IoSupplier<InputStream> getResource(PackType type, Identifier id) {
		if (FONT_PATH.equals(id.getPath())) {
			return delegate.getResource(type, id);
		}
		return null;
	}

	@Override
	public void listResources(PackType type, String namespace, String path, PackResources.ResourceOutput output) {
		delegate.listResources(type, namespace, path, (id, resource) -> {
			if (FONT_PATH.equals(id.getPath())) {
				output.accept(id, resource);
			}
		});
	}

	@Override
	public Set<String> getNamespaces(PackType type) {
		return delegate.getNamespaces(type);
	}

	@Override
	public <T> T getMetadataSection(MetadataSectionType<T> type) throws java.io.IOException {
		return delegate.getMetadataSection(type);
	}

	@Override
	public String packId() {
		return delegate.packId();
	}

	@Override
	public PackLocationInfo location() {
		return delegate.location();
	}

	@Override
	public void close() {
		// delegate.close() 를 호출하지 않고 비워둠
	}
}
