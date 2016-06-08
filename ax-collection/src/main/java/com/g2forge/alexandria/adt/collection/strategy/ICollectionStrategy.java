package com.g2forge.alexandria.adt.collection.strategy;

import com.g2forge.alexandria.adt.collection.collector.ICollectionBuilder;

public interface ICollectionStrategy<C, T> {
	public ICollectionBuilder<C, T> builder();
	
	public Iterable<T> iterable(C collection);
}
