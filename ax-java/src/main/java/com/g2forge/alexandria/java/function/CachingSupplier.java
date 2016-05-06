package com.g2forge.alexandria.java.function;

import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CachingSupplier<T> implements Supplier<T> {
	/** The supplier to cache. Set to <code>null</code> after use to indicate that we already have the result. */
	protected Supplier<? extends T> supplier;

	protected T value;

	public CachingSupplier(Supplier<? extends T> supplier) {
		this.supplier = supplier;
		this.value = null;
	}

	@Override
	public T get() {
		if (supplier != null) {
			value = supplier.get();
			supplier = null;
		}
		return value;
	}

}
