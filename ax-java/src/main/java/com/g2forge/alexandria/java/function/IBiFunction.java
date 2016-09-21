package com.g2forge.alexandria.java.function;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IBiFunction<I0, I1, O> extends BiFunction<I0, I1, O> {
	public default IFunction<I1, O> curry0(I0 input0) {
		return input1 -> this.apply(input0, input1);
	}

	public default IFunction<I0, O> curry1(I1 input1) {
		return input0 -> this.apply(input0, input1);
	}
}
