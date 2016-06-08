package com.g2forge.alexandria.generic.java.map.implementations;

import java.util.Map;

import com.g2forge.alexandria.generic.java.map.IMap1;

public class JAPIMap<I, O> implements IMap1<I, O> {
	protected final Map<I, O> map;
	
	/**
	 * @param map
	 */
	public JAPIMap(final Map<I, O> map) {
		this.map = map;
	}
	
	@Override
	public O map(final I input) {
		return map.get(input);
	}
}
