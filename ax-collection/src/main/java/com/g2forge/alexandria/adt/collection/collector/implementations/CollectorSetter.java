package com.g2forge.alexandria.adt.collection.collector.implementations;

import com.g2forge.alexandria.adt.collection.collector.ICollector;
import com.g2forge.alexandria.java.tuple.ITuple1_S;

public class CollectorSetter<T> implements ITuple1_S<T> {
	protected final ICollector<? super T> builder;

	protected boolean used = false;

	public CollectorSetter(ICollector<? super T> builder) {
		this.builder = builder;
	}

	@Override
	public ITuple1_S<T> set0(final T value) {
		if (used) throw new IllegalStateException("Cannot use a setter more than once!");
		builder.add(value);
		used = true;
		return this;
	}
}
