package com.g2forge.alexandria.collection.strategy.implementations;

import java.util.ArrayList;
import java.util.Collection;

import com.g2forge.alexandria.collection.strategy.ACollectionStrategy;

public class CollectionStrategy<T> extends ACollectionStrategy<Collection<T>, T> {
	protected ArrayList<T> create(Collection<T> copy) {
		if (copy == null) return new ArrayList<>();
		return new ArrayList<>(copy);
	}
}
