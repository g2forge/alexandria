package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface IFunction<I, O> extends Function<I, O> {
	public static <T> IFunction<T, T> identity() {
		return t -> t;
	}

	public default Supplier<O> compose(Supplier<? extends I> before) {
		Objects.requireNonNull(before);
		return () -> apply(before.get());
	}

	public default Supplier<O> curry(I input) {
		return () -> apply(input);
	}

	public default IConsumer<I> noReturn() {
		return i -> apply(i);
	}
}
