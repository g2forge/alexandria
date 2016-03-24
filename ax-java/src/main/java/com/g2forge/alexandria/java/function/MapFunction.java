package com.g2forge.alexandria.java.function;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface MapFunction<I, O> extends Function<I, Optional<? extends O>> {
	public static <I, O> MapFunction<I, O> empty() {
		return i -> Optional.empty();
	}

	public static <I, O> MapFunction<I, O> of(I input, O output) {
		return of(input, () -> output);
	}

	public static <I, O> MapFunction<I, O> of(I input, Supplier<? extends O> output) {
		return i -> input.equals(i) ? Optional.of(output.get()) : Optional.empty();
	}

	public static <I, O> MapFunction<I, O> of(Map<? super I, ? extends O> map) {
		return i -> map.containsKey(i) ? Optional.of(map.get(i)) : Optional.empty();
	}

	public default MapFunction<I, O> override(MapFunction<? super I, ? extends O> output) {
		return i -> {
			final Optional<? extends O> o = output.apply(i);
			return o.isPresent() ? o : apply(i);
		};
	}
}
