package com.g2forge.alexandria.java.fluent.optional.factory;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.g2forge.alexandria.java.fluent.optional.NonNullOptional;
import com.g2forge.alexandria.java.fluent.optional.function.IOptionalFunction;

public interface IOptionalFunctionFactory<I, O, R> {
	public default OptionalFunctionBuilder<I, O, R> build() {
		return new OptionalFunctionBuilder<I, O, R>(this);
	}

	public default R empty() {
		return wrap(IOptionalFunction.empty(getOptionalFactory()));
	}

	public default IOptionalFactory getOptionalFactory() {
		return NonNullOptional.FACTORY;
	}

	public default R of(Map<? super I, ? extends O> map) {
		return wrap(IOptionalFunction.of(getOptionalFactory(), map));
	}

	public default R of(Object input, O output) {
		return wrap(IOptionalFunction.of(getOptionalFactory(), input, output));
	}

	public default R of(Object input, Supplier<? extends O> output) {
		return wrap(IOptionalFunction.of(getOptionalFactory(), input, output));
	}

	public default R of(Predicate<? super I> input, Supplier<? extends O> output) {
		return wrap(IOptionalFunction.of(getOptionalFactory(), input, output));
	}

	public R wrap(IOptionalFunction<I, O> function);
}