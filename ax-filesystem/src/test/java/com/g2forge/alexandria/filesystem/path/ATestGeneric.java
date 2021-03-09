package com.g2forge.alexandria.filesystem.path;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

public class ATestGeneric {
	protected static final IGenericFileSystemInternal<GenericPath> fileSystem = new GenericFileSystem(new IGenericFileSystemProviderInternal<GenericPath>() {
		@Override
		public FileSystemProvider asFileSystemProvider() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void close() {}

		@Override
		public Set<String> getSupportedFileAttributeViews() {
			throw new UnsupportedOperationException();
		}

		@Override
		public URI toURI(GenericPath path) throws URISyntaxException {
			return new URI("test", path.toString(), null);
		}
	}, null).getInternal();

	protected static GenericPath create(final String root, final String... names) {
		return new GenericPath(fileSystem, root, names);
	}

	protected static GenericPath parse(final String string) {
		return (GenericPath) fileSystem.asFileSystem().getPath(string);
	}
}
