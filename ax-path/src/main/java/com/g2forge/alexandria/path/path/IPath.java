package com.g2forge.alexandria.path.path;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.java.adt.compare.CollectionComparator;
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

	public default boolean endsWith(T component) {
		if (isEmpty()) return false;
		return getLast().equals(component);
	}

	public default T getComponent(int index) {
		return HCollection.get(getComponents().toCollection(), index);
	}

	public ICollection<T> getComponents();

	public default T getFirst() {
		return HCollection.getFirst(getComponents().toCollection());
	}

	public default T getLast() {
		return HCollection.getLast(getComponents().toCollection());
	}

	public IPath<T> getParent();

	/**
	 * Extract a portion of this path. Negative indices are measured from the end, with {@code -2} indicating the last component of the path. This means
	 * {@code subPath(-2, -1)} returns a path consisting only of the last component.
	 * 
	 * @param fromIndex The index of the first component to include (inclusive).
	 * @param toIndex The index of the first component to exclude (exclusive).
	 * @return A portion of this path.
	 */
	public IPath<T> subPath(int fromIndex, int toIndex);

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

	public default boolean startsWith(T component) {
		if (isEmpty()) return false;
		return getFirst().equals(component);
	}
}
