package com.g2forge.alexandria.java.core.helpers;

import java.util.function.Supplier;

public class ClassHelpers {
	public static <T> T cast(Class<T> klass, Object object, Supplier<? extends T> supplier) {
		try {
			return klass.cast(object);
		} catch (ClassCastException exception) {
			return supplier.get();
		}
	}
}
