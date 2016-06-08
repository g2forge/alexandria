package com.g2forge.alexandria.adt.collection.strategy;

import java.util.Collection;

import com.g2forge.alexandria.adt.collection.collector.ICollectionBuilder;
import com.g2forge.alexandria.adt.collection.collector.implementations.CollectionCollector;

public abstract class ACollectionStrategy<C extends Collection<T>, T> implements ICollectionStrategy<C, T> {
	protected class Builder extends CollectionCollector<C, T> {
		public Builder() {
			super(ACollectionStrategy.this.create(null));
		}

		@Override
		public C create() {
			return ACollectionStrategy.this.create(collection);
		}
	}

	@Override
	public ICollectionBuilder<C, T> builder() {
		return new Builder();
	}

	protected abstract C create(C copy);

	@Override
	public Iterable<T> iterable(C collection) {
		return collection;
	}
}
