package com.g2forge.alexandria.java.function;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface OptionalFunction<I, O> extends Function<I, Optional<? extends O>> {
	public static <I, O> OptionalFunction<I, O> empty() {
		return i -> Optional.empty();
	}

	public static <I, O> OptionalFunction<I, O> of(I input, O output) {
		return of(input, () -> output);
	}

	public static <I, O> OptionalFunction<I, O> of(I input, Supplier<? extends O> output) {
		return i -> input.equals(i) ? Optional.of(output.get()) : Optional.empty();
	}

	public static <I, O> OptionalFunction<I, O> of(Map<? super I, ? extends O> map) {
		return i -> map.containsKey(i) ? Optional.of(map.get(i)) : Optional.empty();
	}

	public default OptionalFunction<I, O> override(OptionalFunction<? super I, ? extends O> output) {
		return i -> {
			final Optional<? extends O> o = output.apply(i);
			return o.isPresent() ? o : apply(i);
		};
	}

	public default Function<I, O> fallback(Function<? super I, ? extends O> function) {
		return i -> {
			final Optional<? extends O> o = apply(i);
			return o.isPresent() ? o.get() : function.apply(i);
		};
	}
}
