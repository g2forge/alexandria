package com.g2forge.alexandria.java.function;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface IFunction<I, O> extends Function<I, O> {
	public default Supplier<O> curry(I input) {
		return () -> this.apply(input);
	}
}
