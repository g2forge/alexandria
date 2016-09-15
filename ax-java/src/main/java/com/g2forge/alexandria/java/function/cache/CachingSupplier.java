package com.g2forge.alexandria.java.function.cache;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CachingSupplier<I, O> implements Supplier<O> {
	protected final Supplier<? extends I> supplier;

	protected final Function<? super I, ? extends O> function;

	protected I input;

	protected O output;

	@Override
	public O get() {
		final I input = supplier.get();
		if (!Objects.equals(input, this.input)) {
			output = function.apply(input);
			this.input = input;
		}
		return output;
	}
}
