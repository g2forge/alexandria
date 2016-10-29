package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface IFunction<I, O> extends Function<I, O> {
	@SuppressWarnings("unchecked")
	public static <I, O> IFunction<I, O> cast() {
		return i -> (O) i;
	}

	public static <I, O> IFunction<I, O> create(IFunction<I, O> function) {
		return function;
	}

	public static <T> IFunction<T, T> identity() {
		return t -> t;
	}

	@SuppressWarnings("unchecked")
	public static <I, O> IFunction<I, O> isInstanceOf(Class<O> type) {
		return i -> type.isInstance(i) ? (O) i : null;
	}

	public default Supplier<O> compose(Supplier<? extends I> before) {
		Objects.requireNonNull(before);
		return () -> apply(before.get());
	}

	public default Supplier<O> curry(I input) {
		return () -> apply(input);
	}

	public default <T> IFunction<I, T> lift(T equal, IFunction<? super O, ? extends T> lift) {
		return i -> {
			final O o = apply(i);
			if (i == o) return equal;
			return lift.apply(o);
		};
	}

	public default IConsumer<I> noReturn() {
		return i -> apply(i);
	}
}
