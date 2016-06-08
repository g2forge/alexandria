package com.g2forge.alexandria.generic.java.filter;

public interface IFilter<T> {
	public IFilter<T> inverse();
	
	public boolean isAccepted(T value);
}
