package com.g2forge.alexandria.java.function;

import java.util.function.Supplier;

@FunctionalInterface
public interface ISupplier<T> extends Supplier<T> {
	public default <I> IFunction<I, T> toFunction() {
		return t -> get();
	}
}
