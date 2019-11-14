package com.g2forge.alexandria.java.function.builder;

import com.g2forge.alexandria.java.function.ISupplier;

@FunctionalInterface
public interface IBuilder<T> {
	public default ISupplier<T> asSupplier() {
		return this::build;
	}

	public T build();
}
