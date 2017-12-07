package com.g2forge.alexandria.collection;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Sub-interface of {@link ICollection} which allows functional implementation of just the {@link ICollection#iterator()} method.
 * 
 * @author greg
 *
 * @param <T>
 */
@FunctionalInterface
public interface DIteratorCollection<T> extends ICollection<T> {
	public default Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	public default Collection<T> toCollection() {
		return stream().collect(Collectors.toList());
	}
}
