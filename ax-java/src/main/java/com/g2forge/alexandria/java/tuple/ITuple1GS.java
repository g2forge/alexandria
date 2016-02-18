package com.g2forge.alexandria.java.tuple;

public interface ITuple1GS<T0> extends ITuple1G_<T0>, ITuple1_S<T0> {
	@Override
	public ITuple1GS<T0> set0(T0 value);
	
	public T0 swap0(T0 value);
}
