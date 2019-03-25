package com.g2forge.alexandria.java.function.type;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TypeMapIterator<P, C extends P> implements Iterator<C> {
	protected final Iterator<? extends Class<? extends C>> iterator;

	protected final ITypeFunction1<P> function;

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
