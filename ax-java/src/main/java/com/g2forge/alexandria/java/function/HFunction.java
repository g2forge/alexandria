package com.g2forge.alexandria.java.function;

import java.util.function.Function;
import java.util.function.Predicate;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HFunction {
	public static <I, X, O> Function<? super I, ? extends O> compose(Function<? super I, ? extends X> f0, Function<? super X, ? extends O> f1) {
		return f0.andThen(f1);
	}

	public static <I, X0, X1, O> Function<? super I, ? extends O> compose(Function<? super I, ? extends X0> f0, Function<? super X0, ? extends X1> f1, Function<? super X1, ? extends O> f2) {
		return f0.andThen(f1).andThen(f2);
	}

	public static <I0, I1> IBiConsumer<I0, I1> create(IBiConsumer<I0, I1> consumer) {
		return consumer;
	}

	public static <I0, I1, O> IBiFunction<I0, I1, O> create(IBiFunction<I0, I1, O> function) {
		return function;
	}

	public static <I> IConsumer<I> create(IConsumer<I> consumer) {
		return consumer;
	}

	public static <I, O> IFunction<I, O> create(IFunction<I, O> function) {
		return function;
	}

	public static <T> Predicate<T> predicate(boolean value) {
		return t -> value;
	}
}
