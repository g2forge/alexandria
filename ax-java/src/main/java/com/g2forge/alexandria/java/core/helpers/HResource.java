package com.g2forge.alexandria.java.core.helpers;

import java.io.InputStream;

import com.g2forge.alexandria.java.io.HIO;
import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HResource {
	public static String read(Class<?> klass, String resource, boolean newline) {
		final InputStream stream = getResourceAsStream(klass, resource);
		if (stream == null) throw new NullPointerException(String.format("Resource \"%1$s\" could not found relative to class %2$s", resource, klass.getName()));
		return HIO.readAll(stream, newline);
	}

	public static InputStream getResourceAsStream(Class<?> klass, String resource) {
		return klass.getResourceAsStream(resource);
	}
}
