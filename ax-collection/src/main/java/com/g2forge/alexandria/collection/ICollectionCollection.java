package com.g2forge.alexandria.collection;

import java.util.Iterator;
import java.util.stream.Stream;

@FunctionalInterface
public interface ICollectionCollection<T> extends ICollection<T> {
	public default Iterator<T> iterator() {
		return toCollection().iterator();
	}

	public default Stream<T> stream() {
		return toCollection().stream();
	}
}
