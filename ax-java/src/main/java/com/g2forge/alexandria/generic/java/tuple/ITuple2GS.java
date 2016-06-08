package com.g2forge.alexandria.generic.java.tuple;

public interface ITuple2GS<T0, T1> extends ITuple2G_<T0, T1>, ITuple2_S<T0, T1>, ITuple1GS<T0> {
	@Override
	public ITuple2GS<T0, T1> set0(T0 value);
	
	@Override
	public ITuple2GS<T0, T1> set1(T1 value);
	
	public T1 swap1(T1 value);
}
