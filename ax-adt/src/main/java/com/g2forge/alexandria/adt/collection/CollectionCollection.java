package com.g2forge.alexandria.adt.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CollectionCollection<T> implements ICollection<T> {
	protected final Collection<T> collection;

	@Override
	public Iterator<T> iterator() {
		return collection.iterator();
	}

	@Override
	public Stream<T> stream() {
		return collection.stream();
	}

	@Override
	public Collection<T> toCollection() {
		return Collections.unmodifiableCollection(collection);
	}
}
