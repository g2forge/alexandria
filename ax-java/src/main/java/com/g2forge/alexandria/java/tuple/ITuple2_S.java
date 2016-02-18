package com.g2forge.alexandria.java.tuple;

public interface ITuple2_S<T0, T1> extends ITuple2__<T0, T1>, ITuple1_S<T0> {
	@Override
	public ITuple2_S<T0, T1> set0(T0 value);
	
	public ITuple2_S<T0, T1> set1(T1 value);
}
