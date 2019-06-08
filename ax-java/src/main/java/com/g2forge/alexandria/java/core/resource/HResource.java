package com.g2forge.alexandria.java.core.resource;

import java.io.InputStream;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HResource {
	public static String read(Class<?> klass, String resource, boolean newline) {
		return new Resource(klass, resource).read(newline);
	}

	public static InputStream getResourceAsStream(Class<?> klass, String resource) {
		return new Resource(klass, resource).getResourceAsStream();
	}
}
