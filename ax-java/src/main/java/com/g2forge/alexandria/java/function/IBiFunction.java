package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@FunctionalInterface
public interface IBiFunction<I0, I1, O> extends BiFunction<I0, I1, O> {
	public static <I0, I1, O> IBiFunction<I0, I1, O> create(IBiFunction<I0, I1, O> function) {
		return function;
	}

	public default IFunction<I1, O> compose0(Supplier<? extends I0> before) {
		Objects.requireNonNull(before);
		return i1 -> apply(before.get(), i1);
	}

	public default IFunction<I0, O> compose1(Supplier<? extends I1> before) {
		Objects.requireNonNull(before);
		return i0 -> apply(i0, before.get());
	}

	public default IFunction<I1, O> curry0(I0 input0) {
		return input1 -> apply(input0, input1);
	}

	public default IFunction<I0, O> curry1(I1 input1) {
		return input0 -> apply(input0, input1);
	}

	public default IBiConsumer<I0, I1> noReturn() {
		return (i0, i1) -> apply(i0, i1);
	}
}
