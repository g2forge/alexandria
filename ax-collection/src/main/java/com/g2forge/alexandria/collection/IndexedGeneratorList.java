package com.g2forge.alexandria.collection;

import java.util.AbstractList;
import java.util.function.IntFunction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndexedGeneratorList<T> extends AbstractList<T> {
	protected final IntFunction<T> generator;

	protected final int base;

	protected final int size;

	@Override
	public T get(int index) {
		return generator.apply(base + index);
	}

	@Override
	public int size() {
		return size;
	}
}
