package com.g2forge.alexandria.path.path;

import java.util.Collection;

import com.g2forge.alexandria.collection.CollectionCollection;
import com.g2forge.alexandria.collection.EmptyCollection;
import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Path<T> implements IPath<T> {
	protected static final Path<Object> EMPTY = new Path<>();

	@SuppressWarnings("unchecked")
	public static <T> Path<T> createEmpty() {
		return (Path<T>) EMPTY;
	}

	protected final ICollection<T> components;

	public Path(Collection<T> components) {
		this(components.size() < 1 ? EmptyCollection.create() : new CollectionCollection<>(components));
	}

	@SafeVarargs
	public Path(T... components) {
		this(components.length < 1 ? EmptyCollection.create() : new CollectionCollection<>(components));
	}

	@Override
	public IPath<T> resolve(IPath<T> subpath) {
		if (isEmpty()) return subpath;
		if (subpath.isEmpty()) return this;
		return new Path<>(HCollection.concatenate(getComponents().toCollection(), subpath.getComponents().toCollection()));
	}
}
