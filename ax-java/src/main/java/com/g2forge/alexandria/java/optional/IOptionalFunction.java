package com.g2forge.alexandria.java.optional;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface IOptionalFunction<I, O> extends Function<I, IOptional<? extends O>> {
	public static <I, O> IOptionalFunction<I, O> empty(IOptionalFactory factory) {
		return i -> factory.empty();
	}

	public static <I, O> IOptionalFunction<I, O> of(IOptionalFactory factory, I input, O output) {
		return of(factory, input, () -> output);
	}

	public static <I, O> IOptionalFunction<I, O> of(IOptionalFactory factory, I input, Supplier<? extends O> output) {
		return i -> input.equals(i) ? factory.of(output.get()) : factory.empty();
	}

	public static <I, O> IOptionalFunction<I, O> of(IOptionalFactory factory, Map<? super I, ? extends O> map) {
		return i -> map.containsKey(i) ? factory.of(map.get(i)) : factory.empty();
	}

	/**
	 * Create a function which will fall back to the return values from <code>fallback</code> when this function doesn't return a value.
	 * 
	 * @param fallback
	 * @return
	 */
	public default Function<I, O> fallback(Function<? super I, ? extends O> fallback) {
		return i -> {
			final IOptional<? extends O> o = apply(i);
			return o.isPresent() ? o.get() : fallback.apply(i);
		};
	}

	/**
	 * Create a function where any value returned from <code>override</code> overrides the value this function would return.
	 * 
	 * @param override
	 * @return
	 */
	public default IOptionalFunction<I, O> override(IOptionalFunction<? super I, ? extends O> override) {
		return i -> {
			final IOptional<? extends O> o = override.apply(i);
			return o.isPresent() ? o : apply(i);
		};
	}
}
