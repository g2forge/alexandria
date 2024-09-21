package com.g2forge.alexandria.collection.collector.implementations;

import com.g2forge.alexandria.collection.collector.ICollector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SeparatingCollector<T> implements ICollector<T> {
	protected class NestedCollector implements ICollector<T> {
		protected boolean first = true;

		@Override
		public ICollector<T> add(T value) {
			if (first) {
				first = false;
				SeparatingCollector.this.add(value);
			} else collector.add(value);
			return this;
		}
	}

	protected final ICollector<T> collector;

	@Getter
	protected final T separator;

	protected boolean first = true;

	@Override
	public ICollector<T> add(T value) {
		if (first) first = false;
		else collector.add(getSeparator());
		collector.add(value);
		return this;
	}

	public ICollector<T> nested() {
		return new NestedCollector();
	}
}
