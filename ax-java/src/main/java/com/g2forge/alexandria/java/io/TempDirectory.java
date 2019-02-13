package com.g2forge.alexandria.java.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TempDirectory extends CloseablePath {
	protected static Path createPath(Path parent, String prefix) {
		try {
			if (parent != null) Files.createDirectories(parent);
			return parent == null ? Files.createTempDirectory(prefix) : Files.createTempDirectory(parent, prefix);
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}

	public TempDirectory() {
		this(null, null, true);
	}

	public TempDirectory(Path parent, String prefix, boolean autodelete) {
		super(createPath(parent, prefix == null ? TempDirectory.class.getSimpleName() : prefix), autodelete);
	}
}
