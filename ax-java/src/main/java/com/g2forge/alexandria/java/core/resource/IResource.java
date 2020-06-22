package com.g2forge.alexandria.java.core.resource;

import java.io.InputStream;
import java.net.URL;

import com.g2forge.alexandria.java.io.HTextIO;

public interface IResource {
	public Class<?> getKlass();

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
