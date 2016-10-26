package com.g2forge.alexandria.java.function.tri;

import java.util.Objects;

import com.g2forge.alexandria.java.function.IBiConsumer;

@FunctionalInterface
public interface ITriConsumer<I0, I1, I2> {
	public static <I0, I1, I2> ITriConsumer<I0, I1, I2> create(ITriConsumer<I0, I1, I2> consumer) {
		return consumer;
	}

	public void accept(I0 i0, I1 i1, I2 i2);

	public default ITriConsumer<I0, I1, I2> andThen(ITriConsumer<? super I0, ? super I1, ? super I2> after) {
		Objects.requireNonNull(after);

		return (i0, i1, i2) -> {
			accept(i0, i1, i2);
			after.accept(i0, i1, i2);
		};
	}

	public default IBiConsumer<I1, I2> curry0(I0 input0) {
		return (input1, input2) -> this.accept(input0, input1, input2);
	}

	public default IBiConsumer<I0, I2> curry1(I1 input1) {
		return (input0, input2) -> this.accept(input0, input1, input2);
	}

	public default IBiConsumer<I0, I1> curry2(I2 input2) {
		return (input0, input1) -> this.accept(input0, input1, input2);
	}
}
