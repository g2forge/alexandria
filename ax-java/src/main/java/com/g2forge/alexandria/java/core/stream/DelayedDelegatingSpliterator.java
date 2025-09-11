package com.g2forge.alexandria.java.core.stream;

import java.util.Spliterator;

import com.g2forge.alexandria.java.function.ISupplier;

public class DelayedDelegatingSpliterator<T, S extends Spliterator<T>> extends ADelegatingSpliterator<T, S> {
	protected final int characteristics;

	public DelayedDelegatingSpliterator(ISupplier<? extends S> supplier, int characteristics) {
		super(supplier);
		this.characteristics = characteristics;
	}

	@Override
	public int characteristics() {
		return characteristics;
	}

	@Override
	public long estimateSize() {
		return Long.MAX_VALUE;
	}

	@Override
	public long getExactSizeIfKnown() {
		return -1;
	}
}