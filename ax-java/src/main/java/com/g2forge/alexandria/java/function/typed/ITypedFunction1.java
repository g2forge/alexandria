package com.g2forge.alexandria.java.function.typed;

import java.util.function.Supplier;

@FunctionalInterface
public interface ITypedFunction1<T> {
	public static <T> ITypedFunction1<T> create(ITypedFunction1<T> function) {
		return function;
	}

	public default <_T extends T> Supplier<_T> curry(Class<_T> input) {
		return () -> apply(input);
	}

	public <_T extends T> _T apply(Class<_T> type);
}
