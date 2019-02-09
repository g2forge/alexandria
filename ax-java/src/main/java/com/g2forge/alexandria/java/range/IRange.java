package com.g2forge.alexandria.java.range;

public interface IRange<T extends Comparable<T>> {
	public T getMin();

	public T getMax();

	public default boolean isReversed() {
		return compare(getMin(), getMax()) > 0;
	}

	public default int compare(T left, T right) {
		if (left != null) return left.compareTo(right);
		if (right != null) return -right.compareTo(left);
		throw new NullPointerException();
	}
	
	public default boolean isEmpty() {
		return compare(getMin(), getMax()) == 0;
	}

	public default void validateSubRange(IRange<T> subrange) {
		if (isReversed() != subrange.isReversed()) throw new IllegalArgumentException();
		if ((compare(subrange.getMin(), getMin()) < 0) || (compare(subrange.getMin(), getMax()) > (isEmpty() ? 0 : -1))) throw new IllegalArgumentException(String.format("Minimum %1$s is out of range [%2$s,%3$s)!", subrange.getMin(), getMin(), getMax()));
		if ((compare(subrange.getMax(), getMin()) < 0) || (compare(subrange.getMax(), getMax()) > 0)) throw new IllegalArgumentException(String.format("Maximum %1$s is out of range [%2$s,%3$s]!", subrange.getMax(), getMin(), getMax()));
		if (compare(subrange.getMin(), subrange.getMax()) > 0) throw new IllegalArgumentException(String.format("Minimun %1$s is not less than maximum %2$s", subrange.getMin(), subrange.getMax()));
	}
}
