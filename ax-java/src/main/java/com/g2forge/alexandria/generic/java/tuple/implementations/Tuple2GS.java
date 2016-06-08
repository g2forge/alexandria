package com.g2forge.alexandria.generic.java.tuple.implementations;

import com.g2forge.alexandria.generic.java.tuple.ITuple2GS;

public class Tuple2GS<T0, T1> extends Tuple1GS<T0> implements ITuple2GS<T0, T1> {
	protected T1 value1;
	
	public Tuple2GS() {
		this(null, null);
	}
	
	/**
	 * @param value1
	 */
	public Tuple2GS(final T0 value0, final T1 value1) {
		super(value0);
		this.value1 = value1;
	}
	
	@Override
	public T1 get1() {
		return value1;
	}
	
	@Override
	public ITuple2GS<T0, T1> set0(final T0 value) {
		value0 = value;
		return this;
	}
	
	@Override
	public ITuple2GS<T0, T1> set1(final T1 value) {
		this.value1 = value;
		return this;
	}
	
	@Override
	public T1 swap1(final T1 value) {
		final T1 retVal = value1;
		set1(value);
		return retVal;
	}
}
