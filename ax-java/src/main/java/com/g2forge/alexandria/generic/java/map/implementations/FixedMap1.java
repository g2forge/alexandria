package com.g2forge.alexandria.generic.java.map.implementations;

import com.g2forge.alexandria.generic.java.map.IMap1;

public class FixedMap1<I, O> implements IMap1<I, O> {
	protected final O output;
	
	/**
	 * @param output
	 */
	public FixedMap1(O output) {
		this.output = output;
	}

	@Override
	public O map(I input) {
		return output;
	}
	
}
