package com.g2forge.alexandria.collection.strategy;

import com.g2forge.alexandria.collection.collector.ICollectionBuilder;

public interface ICollectionStrategy<C, T> {
	public ICollectionBuilder<C, T> builder();

	public Iterable<T> iterable(C collection);
}
