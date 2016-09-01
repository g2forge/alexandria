package com.g2forge.alexandria.java.function.typed;

@FunctionalInterface
public interface TypedFunction<T> {
	public <_T extends T> _T map(Class<_T> type);
}
