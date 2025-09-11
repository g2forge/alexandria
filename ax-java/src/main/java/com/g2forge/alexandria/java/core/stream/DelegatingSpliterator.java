package com.g2forge.alexandria.java.core.stream;

import java.util.Spliterator;

import com.g2forge.alexandria.java.function.ISupplier;

public class DelegatingSpliterator<T, S extends Spliterator<T>> extends ADelegatingSpliterator<T, S> {
	public DelegatingSpliterator(ISupplier<? extends S> supplier) {
		super(supplier);
	}

	@Override
	public int characteristics() {
		return get().characteristics();
	}

	@Override
	public long estimateSize() {
		return get().estimateSize();
	}

	@Override
	public long getExactSizeIfKnown() {
		return get().getExactSizeIfKnown();
	}

	@Override
	public String toString() {
		return getClass().getName() + "[" + get() + "]";
	}
}