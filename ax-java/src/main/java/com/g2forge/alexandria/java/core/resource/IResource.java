package com.g2forge.alexandria.java.core.resource;

import java.io.InputStream;
import java.net.URL;

import com.g2forge.alexandria.java.io.HIO;

public interface IResource {
	public Class<?> getKlass();

	public String getResource();

	public default InputStream getResourceAsStream() {
		return getKlass().getResourceAsStream(getResource());
	}

	public default URL getURL() {
		return getKlass().getResource(getResource());
	}

	public default String read(boolean newline) {
		final InputStream stream = getResourceAsStream();
		if (stream == null) throw new NullPointerException(String.format("Resource \"%1$s\" could not found relative to class %2$s", getResource(), getKlass().getName()));
		return HIO.readAll(stream, newline);
	}
}
