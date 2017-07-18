package com.g2forge.alexandria.collection;

import java.util.Collection;
import java.util.stream.Stream;

public interface ICollection<T> extends Iterable<T> {
	public Stream<T> stream();

	public Collection<T> toCollection();
}
