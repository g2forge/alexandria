package com.g2forge.alexandria.adt.range;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate1;

public interface IRange<T extends Comparable<T>> {
	public static <T extends Comparable<T>> List<IRange<T>> coalesce(List<? extends IRange<T>> ranges, IFunction2<? super T, ? super T, ? extends IRange<T>> constructor) {
		for (IRange<T> range : ranges) {
			if (range.isReversed()) throw new IllegalArgumentException();
		}
		final List<IRange<T>> sorted = ranges.stream().filter(IPredicate1.<IRange<T>>create(IRange::isEmpty).negate()).sorted((r0, r1) -> r0.getMin().compareTo(r1.getMin())).collect(Collectors.toList());
		final List<IRange<T>> retVal = new ArrayList<>();
		IRange<T> current = sorted.get(0);
		for (IRange<T> range : sorted) {
			if (current.getMax().compareTo(range.getMin()) < 0) {
				retVal.add(current);
				current = range;
			} else current = constructor.apply(current.getMin(), range.getMax());
		}
		retVal.add(current);
		return retVal;
	}

	public default int compare(T left, T right) {
		if (left != null) return left.compareTo(right);
		if (right != null) return -right.compareTo(left);
		throw new NullPointerException();
	}

	public T getMax();

	public T getMin();

	public default boolean isEmpty() {
		return compare(getMin(), getMax()) == 0;
	}

	public default boolean isReversed() {
		return compare(getMin(), getMax()) > 0;
	}

	public default void validateSubRange(IRange<T> subrange) {
		if (isReversed() != subrange.isReversed()) throw new IllegalArgumentException();
		if ((compare(subrange.getMin(), getMin()) < 0) || (compare(subrange.getMin(), getMax()) > (isEmpty() ? 0 : -1))) throw new IllegalArgumentException(String.format("Minimum %1$s is out of range [%2$s,%3$s)!", subrange.getMin(), getMin(), getMax()));
		if ((compare(subrange.getMax(), getMin()) < 0) || (compare(subrange.getMax(), getMax()) > 0)) throw new IllegalArgumentException(String.format("Maximum %1$s is out of range [%2$s,%3$s]!", subrange.getMax(), getMin(), getMax()));
		if (compare(subrange.getMin(), subrange.getMax()) > 0) throw new IllegalArgumentException(String.format("Minimun %1$s is not less than maximum %2$s", subrange.getMin(), subrange.getMax()));
	}
}
