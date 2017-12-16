package com.g2forge.alexandria.adt.collection.collector.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.g2forge.alexandria.adt.collection.collector.ICollectionBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CollectionCollectionBuilder<C extends Collection<? super T>, T> implements ICollectionBuilder<C, T> {
	public static <T> ICollectionBuilder<List<T>, T> createList() {
		return new CollectionCollectionBuilder<>(new ArrayList<>());
	}

	protected final C collection;

	@Override
	public ICollectionBuilder<C, T> add(Iterable<? extends T> values) {
		if (values instanceof Collection) collection.addAll((Collection<? extends T>) values);
		else for (T value : values) {
			add(value);
		}
		return this;
	}

	@Override
	public ICollectionBuilder<C, T> add(T value) {
		collection.add(value);
		return this;
	}

	@Override
	public C create() {
		return collection;
	}
}
