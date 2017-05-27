package com.g2forge.alexandria.java.typed;

import java.lang.reflect.Type;

@FunctionalInterface
public interface ITypeRef<T> {
	public static <T> ITypeRef<T> of(Class<T> type) {
		return () -> type;
	}

	public Type getType();
}
