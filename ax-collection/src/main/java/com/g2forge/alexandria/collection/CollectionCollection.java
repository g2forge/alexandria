package com.g2forge.alexandria.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class CollectionCollection<T> implements ICollection<T> {
	protected final Collection<T> elements;

	@SafeVarargs
	public CollectionCollection(T... elements) {
		this(HCollection.asList(elements));
	}

	@Override
	public boolean isEmpty() {
		return getElements().isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return getElements().iterator();
	}

	@Override
	public Stream<T> stream() {
		return getElements().stream();
	}

	@Override
	public Collection<T> toCollection() {
		final Collection<T> elements = getElements();
		if (elements instanceof List) return Collections.unmodifiableList((List<T>) elements);
		if (elements instanceof SortedSet) return Collections.unmodifiableSet((SortedSet<T>) elements);
		if (elements instanceof Set) return Collections.unmodifiableSet((Set<T>) elements);
		return Collections.unmodifiableCollection(elements);
	}
}
