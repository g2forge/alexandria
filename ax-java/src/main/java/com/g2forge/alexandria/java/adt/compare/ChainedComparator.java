package com.g2forge.alexandria.java.adt.compare;

import java.util.Comparator;
import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class ChainedComparator<T> implements Comparator<T> {
	@Singular
	protected final List<? extends Comparator<? super T>> comparators;

	@SafeVarargs
	public ChainedComparator(Comparator<? super T>... comparators) {
		this(HCollection.asList(comparators));
	}

	@Override
	public int compare(T o1, T o2) {
		for (Comparator<? super T> comparator : getComparators()) {
			final int retVal = comparator.compare(o1, o2);
			if (retVal != 0) return comparator.compare(o1, o2);
		}
		return 0;
	}
}
