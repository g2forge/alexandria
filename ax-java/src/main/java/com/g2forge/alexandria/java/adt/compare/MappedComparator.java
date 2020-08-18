package com.g2forge.alexandria.java.adt.compare;

import java.util.Comparator;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class MappedComparator<I, O> implements Comparator<I> {
	protected final Function<? super I, ? extends O> function;

	protected final Comparator<? super O> comparator;

	@Override
	public int compare(I i1, I i2) {
		final Function<? super I, ? extends O> function = getFunction();
		final O o1 = function.apply(i1);
		final O o2 = function.apply(i2);
		return getComparator().compare(o1, o2);
	}
}
