package com.g2forge.alexandria.java.function.optional;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import com.g2forge.alexandria.java.fluent.optional.function.IOptionalFunctional;

@FunctionalInterface
public interface OptionalFunction<I, O> extends Function<I, Optional<? extends O>> {
	public static <I, O> OptionalFunction<I, O> empty() {
		return i -> Optional.empty();
	}

	public static <I, O> OptionalFunction<I, O> of(Map<? super I, ? extends O> map) {
		return i -> map.containsKey(i) ? Optional.of(map.get(i)) : Optional.empty();
	}

	public static <I, O> OptionalFunction<I, O> of(Object input, O output) {
		return of(input, () -> output);
	}

	public static <I, O> OptionalFunction<I, O> of(Object input, Supplier<? extends O> output) {
		return i -> input.equals(i) ? Optional.of(output.get()) : Optional.empty();
	}

	/**
	 * Create a function which will fall back to the return values from <code>fallback</code> when this function doesn't return a value.
	 * 
	 * @param fallback The function to call back to when <code>this</code> returns no value.
	 * @return A function which will return values from <code>this</code> or, if needed, the fallback.
	 * @see IOptionalFunctional#fallback(Function)
	 */
	public default Function<I, O> fallback(Function<? super I, ? extends O> fallback) {
		return i -> {
			final Optional<? extends O> o = apply(i);
			return o.isPresent() ? o.get() : fallback.apply(i);
		};
	}

	/**
	 * Create a optional function where any value returned from <code>override</code> overrides the value this function would return.
	 * 
	 * @param override The function whose return values, when any, override our own.
	 * @return An optional function which returns the results of <code>override</code> if any, and the result of this optional function otherwise.
	 * @see IOptionalFunctional#override(IOptionalFunctional)
	 */
	public default OptionalFunction<I, O> override(OptionalFunction<? super I, ? extends O> override) {
		return i -> {
			final Optional<? extends O> o = override.apply(i);
			return o.isPresent() ? o : apply(i);
		};
	}
}
