package com.g2forge.alexandria.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SingleCollection<T> implements ICollection<T> {
	@Getter
	protected final T value;

	@Override
	public Iterator<T> iterator() {
		return stream().iterator();
	}

	@Override
	public Stream<T> stream() {
		return Stream.of(getValue());
	}

	@Override
	public Collection<T> toCollection() {
		return Collections.unmodifiableCollection(HCollection.asList(getValue()));
	}
}
