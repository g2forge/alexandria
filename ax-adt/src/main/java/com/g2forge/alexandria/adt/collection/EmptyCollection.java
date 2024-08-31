package com.g2forge.alexandria.adt.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class EmptyCollection<T> implements ICollection<T>, ISingleton {
	protected static final EmptyCollection<Object> INSTANCE = new EmptyCollection<>();

	@SuppressWarnings("unchecked")
	public static <T> EmptyCollection<T> create() {
		return (EmptyCollection<T>) INSTANCE;
	}

	protected EmptyCollection() {}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public Iterator<T> iterator() {
		return Collections.emptyIterator();
	}

	@Override
	public Stream<T> stream() {
		return Stream.empty();
	}

	@Override
	public Collection<T> toCollection() {
		return Collections.emptyList();
	}
}
