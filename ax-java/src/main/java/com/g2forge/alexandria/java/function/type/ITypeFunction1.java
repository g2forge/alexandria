package com.g2forge.alexandria.java.function.type;

import java.util.function.Supplier;

@FunctionalInterface
public interface ITypeFunction1<T> {
	public static <T> ITypeFunction1<T> create(ITypeFunction1<T> function) {
		return function;
	}

	public default <_T extends T> Supplier<_T> curry(Class<_T> input) {
		return () -> apply(input);
	}

	public <_T extends T> _T apply(Class<_T> type);
}
