package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface IFunction1<I, O> extends Function<I, O>, IFunction<O> {
	@SuppressWarnings("unchecked")
	public static <I, O> IFunction1<I, O> cast() {
		return i -> (O) i;
	}

	public static <I, O> IFunction1<I, O> create(IFunction1<I, O> function) {
		return function;
	}

	public static <T> IFunction1<T, T> identity() {
		return t -> t;
	}

	@SuppressWarnings("unchecked")
	public static <I, O> IFunction1<I, O> isInstanceOf(Class<O> type) {
		return i -> type.isInstance(i) ? (O) i : null;
	}

	public default <X> IFunction1<I, X> andThen(IFunction1<? super O, ? extends X> f) {
		return i -> f.apply(apply(i));
	}

	public default O applyWithFallback(@SuppressWarnings("unchecked") I... inputs) {
		for (I input : inputs) {
			final O retVal = apply(input);
			if (retVal != null) return retVal;
		}
		return null;
	}

	public default Supplier<O> compose(Supplier<? extends I> before) {
		Objects.requireNonNull(before);
		return () -> apply(before.get());
	}

	public default Supplier<O> curry(I input) {
		return () -> apply(input);
	}

	public default <IL> IFunction1<IL, O> lift(IFunction1<IL, ? extends I> lift) {
		return lift.andThen(this);
	}

	public default <T> IFunction1<I, T> lift(T equal, IFunction1<? super O, ? extends T> lift) {
		return i -> {
			final O o = apply(i);
			if (i == o) return equal;
			return lift.apply(o);
		};
	}

	public default IConsumer1<I> noReturn() {
		return i -> apply(i);
	}
}
