package com.g2forge.alexandria.adt.collection;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Sub-interface of {@link ICollection} which allows functional implementation of just the {@link ICollection#toCollection()} method.
 *
 * @param <T> The type of the elements in this collection.
 */
@FunctionalInterface
public interface DCollectionCollection<T> extends ICollection<T> {
	@Override
	public default boolean isEmpty() {
		return toCollection().isEmpty();
	}

	@Override
	public default Iterator<T> iterator() {
		return toCollection().iterator();
	}

	@Override
	public default Stream<T> stream() {
		return toCollection().stream();
	}
}
