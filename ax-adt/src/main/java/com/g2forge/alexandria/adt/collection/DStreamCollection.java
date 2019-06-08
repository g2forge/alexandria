package com.g2forge.alexandria.adt.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Sub-interface of {@link ICollection} which allows functional implementation of just the {@link ICollection#stream()} method.
 *
 * @param <T> The type of the elements in this collection.
 */
@FunctionalInterface
public interface DStreamCollection<T> extends ICollection<T> {
	public default Iterator<T> iterator() {
		return stream().iterator();
	}

	public default Collection<T> toCollection() {
		return stream().collect(Collectors.toList());
	}
}
