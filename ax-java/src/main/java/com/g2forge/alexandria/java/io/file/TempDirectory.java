package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.g2forge.alexandria.java.core.resource.ResourceRenderer;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.Getter;

public class TempDirectory extends CloseablePath {
	protected static Path createPath(Path parent, String prefix) {
		try {
			if (parent != null) Files.createDirectories(parent);
			return parent == null ? Files.createTempDirectory(prefix) : Files.createTempDirectory(parent, prefix);
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}

	@Getter(lazy = true)
	private final ResourceRenderer resource = new ResourceRenderer(get());

	public TempDirectory() {
		this(null, null, true);
	}

	public TempDirectory(Path parent, String prefix, boolean autodelete) {
		super(createPath(parent, prefix == null ? TempDirectory.class.getSimpleName() : prefix), autodelete);
	}
}
