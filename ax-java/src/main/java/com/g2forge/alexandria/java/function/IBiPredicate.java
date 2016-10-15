package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface IBiPredicate<I0, I1> extends BiPredicate<I0, I1> {
	public static <I0, I1> IBiPredicate<I0, I1> create(boolean value) {
		return (i0, i1) -> value;
	}

	public default IPredicate<I1> compose0(Supplier<? extends I0> before) {
		Objects.requireNonNull(before);
		return i1 -> test(before.get(), i1);
	}

	public default IPredicate<I0> compose1(Supplier<? extends I1> before) {
		Objects.requireNonNull(before);
		return i0 -> test(i0, before.get());
	}

	public default IPredicate<I1> curry0(I0 input0) {
		return input1 -> test(input0, input1);
	}

	public default IPredicate<I0> curry1(I1 input1) {
		return input0 -> test(input0, input1);
	}

	public default IBiConsumer<I0, I1> noReturn() {
		return (i0, i1) -> test(i0, i1);
	}
}
