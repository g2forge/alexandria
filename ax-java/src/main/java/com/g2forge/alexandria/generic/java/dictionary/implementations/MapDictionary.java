package com.g2forge.alexandria.generic.java.dictionary.implementations;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.g2forge.alexandria.generic.java.dictionary.IDictionary;
import com.g2forge.alexandria.java.tuple.ITuple2GS;

public class MapDictionary<I, O> implements IDictionary<I, O> {
	protected final Map<I, O> map;
	
	/**
	 * @param map
	 */
	public MapDictionary(Map<I, O> map) {
		this.map = map;
	}
	
	@Override
	public O apply(I input) {
		return map.get(input);
	}
	
	@Override
	public Set<I> getInputs() {
		return map.keySet();
	}
	
	@Override
	public Collection<O> getOutputs() {
		return map.values();
	}
	
	@Override
	public Collection<ITuple2GS<I, O>> getTuples() {
		return new AbstractCollection<ITuple2GS<I, O>>() {
			@Override
			public Iterator<ITuple2GS<I, O>> iterator() {
				return new Iterator<ITuple2GS<I, O>>() {
					protected final Iterator<Map.Entry<I, O>> iterator = map.entrySet().iterator();
					
					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}
					
					@Override
					public ITuple2GS<I, O> next() {
						final Map.Entry<I, O> entry = iterator.next();
						return new ITuple2GS<I, O>() {
							@Override
							public I get0() {
								return entry.getKey();
							}
							
							@Override
							public O get1() {
								return entry.getValue();
							}
							
							@Override
							public ITuple2GS<I, O> set0(I value) {
								throw new UnsupportedOperationException("Maps do not support changing a key during iteration!");
							}
							
							@Override
							public ITuple2GS<I, O> set1(O value) {
								entry.setValue(value);
								return this;
							}
							
							@Override
							public I swap0(I value) {
								final I retVal = get0();
								set0(value);
								return retVal;
							}
							
							@Override
							public O swap1(O value) {
								final O retVal = get1();
								set1(value);
								return retVal;
							}
						};
					}
					
					@Override
					public void remove() {
						iterator.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return map.size();
			}
		};
	}
}
