package com.g2forge.alexandria.adt.collection.collector;

public interface ICollector<T> {
	public ICollector<T> add(Iterable<? extends T> values);
	
	public ICollector<T> add(T value);
	
	public ICollector<T> add(@SuppressWarnings("unchecked") T... values);
}
