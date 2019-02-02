package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface IConsumer2<I0, I1> extends BiConsumer<I0, I1>, IConsumer {
	public static <I0, I1> IConsumer2<I0, I1> create(IConsumer2<I0, I1> consumer) {
		return consumer;
	}

	public default IConsumer2<I0, I1> andThen(BiConsumer<? super I0, ? super I1> after) {
		Objects.requireNonNull(after);
		return (input0, input1) -> {
			accept(input0, input1);
			after.accept(input0, input1);
		};
	}

	public default IConsumer1<I1> curry0(I0 input0) {
		return input1 -> this.accept(input0, input1);
	}

	public default IConsumer1<I0> curry1(I1 input1) {
		return input0 -> this.accept(input0, input1);
	}

	public default <O> IFunction2<I0, I1, O> toFunction(O retVal) {
		return (i0, i1) -> {
			accept(i0, i1);
			return retVal;
		};
	}
}
