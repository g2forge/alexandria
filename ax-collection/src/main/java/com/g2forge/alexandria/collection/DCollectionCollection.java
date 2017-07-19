package com.g2forge.alexandria.collection;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Sub-interface of {@link ICollection} which allows functional implementation of just the {@link ICollection#toCollection()} method.
 * 
 * @author greg
 *
 * @param <T>
 */
@FunctionalInterface
public interface DCollectionCollection<T> extends ICollection<T> {
	public default Iterator<T> iterator() {
		return toCollection().iterator();
	}

	public default Stream<T> stream() {
		return toCollection().stream();
	}
}
