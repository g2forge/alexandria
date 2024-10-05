package com.g2forge.alexandria.path.path;

import java.util.Collection;
import java.util.List;

import com.g2forge.alexandria.collection.CollectionCollection;
import com.g2forge.alexandria.collection.EmptyCollection;
import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.java.core.error.IllegalOperationException;
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

	protected IPath<T> cast(IPath<T> path) {
		return path;
	}

	protected IPath<T> create(Collection<T> components) {
		return new Path<>(components);
	}

	@Override
	public IPath<T> getParent() {
		if (isEmpty()) throw new IllegalOperationException();
		final List<T> list = HCollection.asList(getComponents().toCollection());
		return create(list.subList(0, list.size() - 1));
	}

	@Override
	public IPath<T> resolve(IPath<T> subpath) {
		if (isEmpty()) return cast(subpath);
		if (subpath.isEmpty()) return this;
		return create(HCollection.concatenate(getComponents().toCollection(), subpath.getComponents().toCollection()));
	}

	@Override
	public IPath<T> subPath(int fromIndex, int toIndex) {
		if (fromIndex < 0) fromIndex += size() + 1;
		if (toIndex < 0) toIndex += size() + 1;
		final List<T> list = HCollection.asList(getComponents().toCollection());
		return create(list.subList(fromIndex, toIndex));
	}
}
