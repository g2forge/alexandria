package com.g2forge.alexandria.adt.collection.collector;

import com.g2forge.alexandria.java.function.ISupplier;

public interface ICollectionBuilder<C, T> extends ICollector<T>, ISupplier<C> {
	@Override
	public default ICollectionBuilder<C, T> add(Iterable<? extends T> values) {
		for (T value : values) {
			add(value);
		}
		return this;
	}

	public ICollectionBuilder<C, T> add(T value);

	@Override
	public default ICollectionBuilder<C, T> add(@SuppressWarnings("unchecked") T... values) {
		for (T value : values) {
			add(value);
		}
		return this;
	}
}
