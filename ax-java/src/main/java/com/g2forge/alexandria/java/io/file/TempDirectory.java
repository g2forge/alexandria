package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.g2forge.alexandria.java.core.resource.IResource;
import com.g2forge.alexandria.java.core.resource.Resource;
import com.g2forge.alexandria.java.io.HBinaryIO;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.Getter;

public class TempDirectory extends CloseablePath {
	public class ResourceHandler {
		public Path resource(Class<?> klass, String resource) {
			return resource(new Resource(klass, resource), resource);
		}

		public void resource(Class<?> klass, String resource, Path path) {
			resource(new Resource(klass, resource), path);
		}

		public Path resource(Class<?> klass, String resource, String local) {
			return resource(new Resource(klass, resource), local == null ? resource : local);
		}

		public Path resource(IResource resource) {
			return resource(resource, resource.getResource());
		}

		public void resource(IResource resource, Path path) {
			try (final InputStream input = resource.getResourceAsStream(true); final OutputStream output = Files.newOutputStream(path)) {
				HBinaryIO.copy(input, output);
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
		}

		public Path resource(IResource resource, String local) {
			final Path retVal = get().resolve(local);
			resource(resource, retVal);
			return retVal;
		}
	}

	protected static Path createPath(Path parent, String prefix) {
		try {
			if (parent != null) Files.createDirectories(parent);
			return parent == null ? Files.createTempDirectory(prefix) : Files.createTempDirectory(parent, prefix);
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}

	@Getter(lazy = true)
	private final ResourceHandler resource = new ResourceHandler();

	public TempDirectory() {
		this(null, null, true);
	}

	public TempDirectory(Path parent, String prefix, boolean autodelete) {
		super(createPath(parent, prefix == null ? TempDirectory.class.getSimpleName() : prefix), autodelete);
	}
}
