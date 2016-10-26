package com.g2forge.alexandria.java.function.tri;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import com.g2forge.alexandria.java.function.IBiFunction;

@FunctionalInterface
public interface ITriFunction<I0, I1, I2, O> {
	public static <I0, I1, I2, O> ITriFunction<I0, I1, I2, O> create(ITriFunction<I0, I1, I2, O> function) {
		return function;
	}

	public default <_O> ITriFunction<I0, I1, I2, _O> andThen(Function<? super O, ? extends _O> after) {
		Objects.requireNonNull(after);
		return (I0 i0, I1 i1, I2 i2) -> after.apply(apply(i0, i1, i2));
	}

	public O apply(I0 i0, I1 i1, I2 i2);

	public default IBiFunction<I1, I2, O> compose0(Supplier<? extends I0> before) {
		Objects.requireNonNull(before);
		return (i1, i2) -> apply(before.get(), i1, i2);
	}

	public default IBiFunction<I0, I2, O> compose1(Supplier<? extends I1> before) {
		Objects.requireNonNull(before);
		return (i0, i2) -> apply(i0, before.get(), i2);
	}

	public default IBiFunction<I0, I1, O> compose2(Supplier<? extends I2> before) {
		Objects.requireNonNull(before);
		return (i0, i1) -> apply(i0, i1, before.get());
	}

	public default IBiFunction<I1, I2, O> curry0(I0 input0) {
		return (input1, input2) -> this.apply(input0, input1, input2);
	}

	public default IBiFunction<I0, I2, O> curry1(I1 input1) {
		return (input0, input2) -> this.apply(input0, input1, input2);
	}

	public default IBiFunction<I0, I1, O> curry2(I2 input2) {
		return (input0, input1) -> this.apply(input0, input1, input2);
	}

	public default ITriConsumer<I0, I1, I2> noReturn() {
		return (i0, i1, i2) -> apply(i0, i1, i2);
	}
}
