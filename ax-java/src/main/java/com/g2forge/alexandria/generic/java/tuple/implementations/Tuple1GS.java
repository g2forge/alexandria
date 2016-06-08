package com.g2forge.alexandria.generic.java.tuple.implementations;

import com.g2forge.alexandria.generic.java.tuple.ITuple1GS;

public class Tuple1GS<T0> implements ITuple1GS<T0> {
	protected T0 value0;
	
	public Tuple1GS() {
		this(null);
	}
	
	/**
	 * @param value0
	 */
	public Tuple1GS(final T0 value0) {
		this.value0 = value0;
	}
	
	@Override
	public T0 get0() {
		return value0;
	}
	
	@Override
	public ITuple1GS<T0> set0(final T0 value) {
		this.value0 = value;
		return this;
	}
	
	@Override
	public T0 swap0(final T0 value) {
		final T0 retVal = value0;
		set0(value);
		return retVal;
	}
}
