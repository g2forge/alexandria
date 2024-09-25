package com.g2forge.alexandria.path.path;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.java.adt.compare.CollectionComparator;
import com.g2forge.alexandria.java.core.error.IllegalOperationException;
import com.g2forge.alexandria.java.core.helpers.HCollection;

public interface IPath<T> {
	public static class PathComparator<T, P extends IPath<? extends T>> implements Comparator<P> {
		protected final CollectionComparator<T, Collection<? extends T>> collectionComparator;

		public PathComparator(final Comparator<? super T> componentComparator) {
			this.collectionComparator = new CollectionComparator<>(componentComparator);
		}

		@Override
		public int compare(P c1, P c2) {
			return collectionComparator.compare(c1.getComponents().toCollection(), c2.getComponents().toCollection());
		}
	}

	public default boolean endsWith(IPath<T> other) {
		final int thisSize = size();
		final int thatSize = other.size();
		if (thisSize < thatSize) return false;
		final List<T> thisEnding = HCollection.asList(getComponents().toCollection()).subList(thisSize - thatSize, thisSize);
		final List<T> thatList = HCollection.asList(other.getComponents().toCollection());
		return thisEnding.equals(thatList);
	}

	public default T getComponent(int index) {
		return HCollection.get(getComponents().toCollection(), index);
	}

	public ICollection<T> getComponents();

	public default IPath<T> getParent() {
		if (isEmpty()) throw new IllegalOperationException();
		final List<T> list = HCollection.asList(getComponents().toCollection());
		return new Path<>(list.subList(0, list.size() - 1));
	}

	public default boolean isEmpty() {
		return getComponents().isEmpty();
	}

	public IPath<T> resolve(IPath<T> subpath);

	public default IPath<T> resolveSibling(IPath<T> subpath) {
		return getParent().resolve(subpath);
	}

	public default int size() {
		if (isEmpty()) return 0;
		return getComponents().toCollection().size();
	}

	public default boolean startsWith(IPath<T> other) {
		final int thisSize = size();
		final int thatSize = other.size();
		if (thisSize < thatSize) return false;
		final List<T> beginning = HCollection.asList(getComponents().toCollection()).subList(0, thatSize);
		return beginning.equals(other.getComponents().toCollection());
	}
}
