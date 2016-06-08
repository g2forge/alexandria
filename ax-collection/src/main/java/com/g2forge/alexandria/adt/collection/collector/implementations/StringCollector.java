package com.g2forge.alexandria.adt.collection.collector.implementations;

import com.g2forge.alexandria.adt.collection.collector.ICollector;
import com.g2forge.alexandria.generic.java.ObjectHelpers;

public class StringCollector<T> implements ICollector<T> {
	protected final StringBuilder internal = new StringBuilder();
	
	@Override
	public ICollector<T> add(final Iterable<? extends T> values) {
		for (final T value : values) {
			add(value);
		}
		return this;
	}
	
	@Override
	public ICollector<T> add(final T value) {
		internal.append(ObjectHelpers.toString(value));
		return this;
	}
	
	@Override
	public ICollector<T> add(@SuppressWarnings("unchecked") final T... values) {
		for (final T value : values) {
			add(value);
		}
		return this;
	}
	
	@Override
	public String toString() {
		return internal.toString();
	}
}
