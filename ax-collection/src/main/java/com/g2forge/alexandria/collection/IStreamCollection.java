package com.g2forge.alexandria.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

@FunctionalInterface
public interface IStreamCollection<T> extends ICollection<T> {
	public default Iterator<T> iterator() {
		return stream().iterator();
	}

	public default Collection<T> toCollection() {
		return stream().collect(Collectors.toList());
	}
}
