package com.g2forge.alexandria.java.function;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FunctionHelpers {
	public static <I, O> Function<I, O> illegal() {
		return input -> {
			throw new IllegalArgumentException(input == null ? "null" : input.toString());
		};
	}

	public static <I, O> Function<? super I, ? extends O> override(Function<? super I, ? extends O> function, I input, O output) {
		return override(function, input, () -> output);
	}

	public static <I, O> Function<? super I, ? extends O> override(Function<? super I, ? extends O> function, I input, Supplier<? extends O> output) {
		return override(function, input::equals, x -> output.get());
	}

	public static <I, O> Function<? super I, ? extends O> override(Function<? super I, ? extends O> function, Predicate<? super I> predicate, Function<? super I, ? extends O> output) {
		return x -> predicate.test(x) ? output.apply(x) : function.apply(x);
	}

	public static <I, O> Function<I, O> unmapped() {
		return input -> {
			throw new UnmappedInputException(input == null ? "null" : input.toString());
		};
	}
}
