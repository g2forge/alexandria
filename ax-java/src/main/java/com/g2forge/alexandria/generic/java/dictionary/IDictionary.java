package com.g2forge.alexandria.generic.java.dictionary;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import com.g2forge.alexandria.java.tuple.ITuple2GS;

public interface IDictionary<I, O> extends Function<I, O> {
	public Set<I> getInputs();
	
	public Collection<O> getOutputs();
	
	public Collection<ITuple2GS<I, O>> getTuples();
}
