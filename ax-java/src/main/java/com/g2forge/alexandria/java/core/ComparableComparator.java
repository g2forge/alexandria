package com.g2forge.alexandria.java.core;

import java.util.Comparator;

public class ComparableComparator<T extends Comparable<? super T>> implements Comparator<T> {
	protected static final ComparableComparator<?> singleton = new ComparableComparator<>();

	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> ComparableComparator<T> getSingleton() {
		return (ComparableComparator<T>) singleton;
	}

	@Override
	public int compare(T o1, T o2) {
		return o1.compareTo(o2);
	}
}
