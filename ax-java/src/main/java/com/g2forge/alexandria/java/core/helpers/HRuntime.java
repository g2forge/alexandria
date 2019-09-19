package com.g2forge.alexandria.java.core.helpers;

import java.net.URL;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HRuntime {
	public static URL whereFrom(Object object) {
		if (object == null) return null;
		final Class<?> type = object.getClass();
		return whereFrom(type);
	}

	public static URL whereFrom(final Class<?> type) {
		final ClassLoader loader;
		if (type.getClassLoader() == null) {
			// Find the bootstrap loader
			ClassLoader current = ClassLoader.getSystemClassLoader();
			while ((current != null) && (current.getParent() != null)) {
				current = current.getParent();
			}
			loader = current;
		} else loader = type.getClassLoader();
		if (loader == null) return null;

		Class<?> top = type;
		while (top.getEnclosingClass() != null) {
			top = top.getEnclosingClass();
		}

		final String name = top.getCanonicalName();
		return loader.getResource(name.replace(".", "/") + ".class");
	}
}
