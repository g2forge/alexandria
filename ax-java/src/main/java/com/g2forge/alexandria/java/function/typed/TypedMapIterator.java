package com.g2forge.alexandria.java.function.typed;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TypedMapIterator<P, C extends P> implements Iterator<C> {
	protected final Iterator<? extends Class<? extends C>> iterator;

	protected final ITypedFunction1<P> function;

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public C next() {
		final Class<? extends C> type = iterator.next();
		return function.apply(type);
	}

	@Override
	public void remove() {
		iterator.remove();
	}
}
