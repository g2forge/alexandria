package com.g2forge.alexandria.generic.java.filter;

import com.g2forge.alexandria.generic.java.filter.implementations.InverseFilter;

public abstract class AFilter<T> implements IFilter<T> {
	@Override
	public IFilter<T> inverse() {
		return new InverseFilter<>(this);
	}
}
