package com.g2forge.alexandria.java.function.cache;

import java.util.function.Supplier;

public class LazySupplier<T> implements Supplier<T> {
	/** The supplier to cache. Set to <code>null</code> after use to indicate that we already have the result. */
	protected Supplier<? extends T> supplier;

	protected T value;

	public LazySupplier(Supplier<? extends T> supplier) {
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
