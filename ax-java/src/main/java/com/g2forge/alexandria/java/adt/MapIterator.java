package com.g2forge.alexandria.java.adt;

import java.util.Iterator;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapIterator<I, O> implements Iterator<O> {
	protected final Iterator<? extends I> iterator;

	protected final Function<? super I, ? extends O> function;

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public O next() {
		return function.apply(iterator.next());
	}

	@Override
	public void remove() {
		iterator.remove();
	}
}
