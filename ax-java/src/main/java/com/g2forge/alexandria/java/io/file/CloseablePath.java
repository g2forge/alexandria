package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.g2forge.alexandria.java.close.AGuaranteeClose;
import com.g2forge.alexandria.java.function.ISupplier;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.Getter;

public class CloseablePath extends AGuaranteeClose {
	@Getter
	protected final Path path;

	protected final boolean autodelete;

	public CloseablePath(ISupplier<Path> path, boolean autodelete) {
		this(path.get(), autodelete);
	}

	public CloseablePath(Path path, boolean autodelete) {
		super(autodelete);
		this.path = path;
		this.autodelete = autodelete;
	}

	@Override
	protected void closeInternal() {
		try {
			if (path != null && Files.exists(path)) {
				HFile.gc();
				HFile.delete(path);
			}
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}
}
