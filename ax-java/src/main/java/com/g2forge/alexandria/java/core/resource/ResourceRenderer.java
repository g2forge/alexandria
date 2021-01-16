package com.g2forge.alexandria.java.core.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.g2forge.alexandria.java.io.HBinaryIO;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ResourceRenderer {
	@Getter(AccessLevel.PROTECTED)
	protected final Path path;

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
		final Path retVal = getPath().resolve(local);
		resource(resource, retVal);
		return retVal;
	}
}