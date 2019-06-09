package com.g2forge.alexandria.java.nestedstate;

public interface ICloseableNestedState<T> extends INestedState<T> {
	public void close(T expected);
}
