package com.g2forge.alexandria.java.adt.compare;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class CollectionComparator<T, C extends Collection<? extends T>> implements Comparator<C> {
	protected final Comparator<? super T> elementComparator;

	@Override
	public int compare(C c1, C c2) {
		final Comparator<? super T> elementComparator = getElementComparator();

		final Iterator<? extends T> i1 = c1.iterator();
		final Iterator<? extends T> i2 = c2.iterator();
		for (int size = Math.min(c1.size(), c2.size()); size > 0; size--) {
			final int result = elementComparator.compare(i1.next(), i2.next());
			if (result != 0) return result;
		}
		return c1.size() - c2.size();
	}
}
