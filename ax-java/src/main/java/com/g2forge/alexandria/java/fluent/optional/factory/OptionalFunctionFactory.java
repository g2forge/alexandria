package com.g2forge.alexandria.java.fluent.optional.factory;

import com.g2forge.alexandria.java.fluent.optional.function.IOptionalFunction;

public class OptionalFunctionFactory<I, O> implements IOptionalFunctionFactory<I, O, IOptionalFunction<I, O>> {
	@Override
	public IOptionalFunction<I, O> wrap(IOptionalFunction<I, O> function) {
		return function;
	}
}