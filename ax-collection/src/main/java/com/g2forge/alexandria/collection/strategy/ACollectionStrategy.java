package com.g2forge.alexandria.collection.strategy;

import java.util.Collection;

import com.g2forge.alexandria.collection.collector.ICollectionBuilder;
import com.g2forge.alexandria.collection.collector.implementations.CollectionCollectionBuilder;

public abstract class ACollectionStrategy<C extends Collection<T>, T> implements ICollectionStrategy<C, T> {
	protected class Builder extends CollectionCollectionBuilder<C, T> {
		public Builder() {
			super(ACollectionStrategy.this.create(null));
		}

		@Override
		public C get() {
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
