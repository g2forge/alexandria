package com.g2forge.alexandria.java.core.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.g2forge.alexandria.java.close.CloseableSupplier;
import com.g2forge.alexandria.java.close.ICloseableSupplier;
import com.g2forge.alexandria.java.io.HTextIO;
import com.g2forge.alexandria.java.project.HProject;

public interface IResource {
	public Class<?> getKlass();

	public default ICloseableSupplier<Path> getPath() {
		final URI uri;
		try {
			uri = getURL().toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		return new CloseableSupplier<Path>(consumer -> {
			try {
				consumer.accept(HProject.openFileSystem(uri));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
			return Paths.get(uri);
		});
	}

	public String getResource();

	public default InputStream getResourceAsStream(boolean assertExists) {
		final InputStream retVal = getKlass().getResourceAsStream(getResource());
		if ((retVal == null) && assertExists) throw new NullPointerException(String.format("Resource \"%1$s\" could not be found relative to class %2$s (if the file exists, check your maven resource configuration)", getResource(), getKlass().getName()));
		return retVal;
	}

	public default URL getURL() {
		return getKlass().getResource(getResource());
	}

	public default boolean isExists() {
		return getResourceAsStream(false) != null;
	}

	public default String read(boolean newline) {
		return HTextIO.readAll(getResourceAsStream(true), newline);
	}
}
