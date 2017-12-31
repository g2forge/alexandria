package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface IPredicate3<I0, I1, I2> {
	public static <I0, I1, I2> IPredicate3<I0, I1, I2> create(boolean value) {
		return (i0, i1, i2) -> value;
	}
	
	public static <I0, I1, I2> IPredicate3<I0, I1, I2> create(IPredicate3<I0, I1, I2> predicate) {
		return predicate;
	}

	public default IPredicate2<I1, I2> compose0(Supplier<? extends I0> before) {
		Objects.requireNonNull(before);
		return (i1, i2) -> test(before.get(), i1, i2);
	}

	public default IPredicate2<I0, I2> compose1(Supplier<? extends I1> before) {
		Objects.requireNonNull(before);
		return (i0, i2) -> test(i0, before.get(), i2);
	}

	public default IPredicate2<I0, I1> compose2(Supplier<? extends I2> before) {
		Objects.requireNonNull(before);
		return (i0, i1) -> test(i0, i1, before.get());
	}

	public default IPredicate2<I1, I2> curry0(I0 input0) {
		return (input1, input2) -> test(input0, input1, input2);
	}

	public default IPredicate2<I0, I2> curry1(I1 input1) {
		return (input0, input2) -> test(input0, input1, input2);
	}

	public default IPredicate2<I0, I1> curry2(I2 input2) {
		return (input0, input1) -> test(input0, input1, input2);
	}

	public default IConsumer3<I0, I1, I2> noReturn() {
		return (i0, i1, i2) -> test(i0, i1, i2);
	}

	public boolean test(I0 i0, I1 i1, I2 i2);
}
