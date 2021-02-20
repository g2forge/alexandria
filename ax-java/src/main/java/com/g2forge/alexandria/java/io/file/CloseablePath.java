package com.g2forge.alexandria.java.io.file;

import java.nio.file.Files;
import java.nio.file.Path;

import com.g2forge.alexandria.java.close.AGuaranteeClose;
import com.g2forge.alexandria.java.close.ICloseableSupplier;
import com.g2forge.alexandria.java.function.ISupplier;

public class CloseablePath extends AGuaranteeClose implements ICloseableSupplier<Path> {
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
		if (path != null && Files.exists(path)) {
			HFile.gc();
			HFile.delete(path, true);
		}
	}

	@Override
	public Path get() {
		return path;
	}
}
