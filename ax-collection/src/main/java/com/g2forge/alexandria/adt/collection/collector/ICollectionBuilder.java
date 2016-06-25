package com.g2forge.alexandria.adt.collection.collector;

import com.g2forge.alexandria.java.core.iface.IFactory;

public interface ICollectionBuilder<C, T> extends ICollector<T>, IFactory<C> {
	public ICollectionBuilder<C, T> add(Iterable<? extends T> values);

	public ICollectionBuilder<C, T> add(T value);

	public ICollectionBuilder<C, T> add(@SuppressWarnings("unchecked") T... values);
}
