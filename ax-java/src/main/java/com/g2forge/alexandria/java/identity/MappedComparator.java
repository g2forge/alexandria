package com.g2forge.alexandria.java.identity;

import java.util.Comparator;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MappedComparator<T, C extends Comparable<C>> implements Comparator<T> {
	protected final Function<? super T, ? extends C> function;

	@Override
	public int compare(T o1, T o2) {
		final C c1 = function.apply(o1);
		final C c2 = function.apply(o2);

		if (c1 == null) {
			if (c2 == null) return 0;
			return -1;
		}
		if (c2 == null) return 1;
		return c1.compareTo(c2);
	}
}
