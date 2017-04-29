package com.g2forge.alexandria.java.core;

import java.util.Comparator;

import com.g2forge.alexandria.java.core.iface.ISingleton;

public class ComparableComparator<T extends Comparable<? super T>> implements Comparator<T>, ISingleton {
	protected static final ComparableComparator<?> singleton = new ComparableComparator<>();

	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> ComparableComparator<T> create() {
		return (ComparableComparator<T>) singleton;
	}

	@Override
	public int compare(T o1, T o2) {
		return o1.compareTo(o2);
	}
}
