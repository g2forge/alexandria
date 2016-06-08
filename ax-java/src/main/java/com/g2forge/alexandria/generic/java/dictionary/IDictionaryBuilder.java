package com.g2forge.alexandria.generic.java.dictionary;

public interface IDictionaryBuilder<I, O> {
	public IDictionaryBuilder<I, O> add(Iterable<? extends I> values);
	
	public IDictionaryBuilder<I, O> add(I input, O output);
}
