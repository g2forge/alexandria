package com.g2forge.alexandria.adt.collection.collector.implementations;

import java.util.Collection;

import com.g2forge.alexandria.adt.collection.collector.ICollectionBuilder;
import com.g2forge.alexandria.adt.collection.collector.ICollector;

public class CollectionCollector<C extends Collection<? super T>, T> implements ICollectionBuilder<C, T> {
	protected final C collection;

	/**
	 * @param collection
	 */
	public CollectionCollector(C collection) {
		this.collection = collection;
	}

	@Override
	public ICollector<T> add(Iterable<? extends T> values) {
		for (T value : values) {
			collection.add(value);
		}
		return this;
	}

	@Override
	public ICollector<T> add(T value) {
		collection.add(value);
		return this;
	}

	@Override
	public ICollector<T> add(@SuppressWarnings("unchecked") T... values) {
		for (T value : values) {
			collection.add(value);
		}
		return this;
	}

	@Override
	public C create() {
		return collection;
	}
}
