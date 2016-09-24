package com.g2forge.alexandria.adt.collection.collector;

public interface ICollector<T> {
	public default ICollector<T> add(Iterable<? extends T> values) {
		for (T value : values) {
			add(value);
		}
		return this;
	}

	public ICollector<T> add(T value);

	public default ICollector<T> add(@SuppressWarnings("unchecked") T... values) {
		for (T value : values) {
			add(value);
		}
		return this;
	}
}
