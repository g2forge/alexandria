package com.g2forge.alexandria.java.function;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface IBiConsumer<I0, I1> extends BiConsumer<I0, I1> {
	public static <I0, I1> IBiConsumer<I0, I1> create(IBiConsumer<I0, I1> consumer) {
		return consumer;
	}

	public default IConsumer<I1> curry0(I0 input0) {
		return input1 -> this.accept(input0, input1);
	}

	public default IConsumer<I0> curry1(I1 input1) {
		return input0 -> this.accept(input0, input1);
	}
}
