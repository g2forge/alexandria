package com.g2forge.alexandria.java.adt.compare;

import java.util.Comparator;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class ComparableComparator<T extends Comparable<? super T>> implements Comparator<T>, ISingleton {
	protected static final ComparableComparator<?> singleton = new ComparableComparator<>();

	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> ComparableComparator<T> create() {
		return (ComparableComparator<T>) singleton;
	}

	@Override
	public int compare(T o1, T o2) {
		if (o1 == null) {
			if (o2 == null) return 0;
			return -1;
		}
		if (o2 == null) return 1;

		return o1.compareTo(o2);
	}
}
