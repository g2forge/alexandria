package com.g2forge.alexandria.generic.java.runtime;

import java.net.URL;

public class RuntimeHelpers {
	public static URL whereFrom(Object object) {
		if (object == null) return null;
		final Class<?> type = object.getClass();
		
		final ClassLoader loader;
		if (type.getClassLoader() == null) {
			// Find the bootstrap loader
			ClassLoader current = ClassLoader.getSystemClassLoader();
			while ((current != null) && (current.getParent() != null)) {
				current = current.getParent();
			}
			loader = current;
		} else {
			loader = type.getClassLoader();
		}
		
		if (loader != null) {
			final String name = type.getCanonicalName();
			return loader.getResource(name.replace(".", "/") + ".class");
		}
		return null;
	}
	
}
