package com.g2forge.alexandria.generic.java.dictionary;

import java.util.Collection;
import java.util.Set;

import com.g2forge.alexandria.generic.java.map.IMap1;
import com.g2forge.alexandria.java.tuple.ITuple2GS;

public interface IDictionary<I, O> extends IMap1<I, O> {
	public Set<I> getInputs();
	
	public Collection<O> getOutputs();
	
	public Collection<ITuple2GS<I, O>> getTuples();
}
