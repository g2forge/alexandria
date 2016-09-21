package com.g2forge.alexandria.java.function.tri;

import java.util.Objects;
import java.util.function.Function;

import com.g2forge.alexandria.java.function.IBiFunction;

@FunctionalInterface
public interface ITriFunction<I0, I1, I2, O> {
	public default <_O> ITriFunction<I0, I1, I2, _O> andThen(Function<? super O, ? extends _O> after) {
		Objects.requireNonNull(after);
		return (I0 i0, I1 i1, I2 i2) -> after.apply(apply(i0, i1, i2));
	}

	public O apply(I0 i0, I1 i1, I2 i2);

	public default IBiFunction<I1, I2, O> curry0(I0 input0) {
		return (input1, input2) -> this.apply(input0, input1, input2);
	}

	public default IBiFunction<I0, I2, O> curry1(I1 input1) {
		return (input0, input2) -> this.apply(input0, input1, input2);
	}

	public default IBiFunction<I0, I1, O> curry2(I2 input2) {
		return (input0, input1) -> this.apply(input0, input1, input2);
	}
}
