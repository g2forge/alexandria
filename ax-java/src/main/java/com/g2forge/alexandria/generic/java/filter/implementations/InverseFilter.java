package com.g2forge.alexandria.generic.java.filter.implementations;

import com.g2forge.alexandria.generic.java.filter.IFilter;

public class InverseFilter<T> implements IFilter<T> {
	protected final IFilter<T> filter;
	
	/**
	 * @param filter
	 */
	public InverseFilter(final IFilter<T> filter) {
		this.filter = filter;
	}
	
	@Override
	public IFilter<T> inverse() {
		return filter;
	}
	
	@Override
	public boolean isAccepted(final T value) {
		return !filter.isAccepted(value);
	}
	
}
