package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface IFunction3<I0, I1, I2, O> extends IFunction<O> {
	public static <I0, I1, I2, O> IFunction3<I0, I1, I2, O> create(IFunction3<I0, I1, I2, O> function) {
		return function;
	}

	public default <_O> IFunction3<I0, I1, I2, _O> andThen(Function<? super O, ? extends _O> after) {
		Objects.requireNonNull(after);
		return (I0 i0, I1 i1, I2 i2) -> after.apply(apply(i0, i1, i2));
	}

	public O apply(I0 i0, I1 i1, I2 i2);

	public default IFunction2<I1, I2, O> compose0(Supplier<? extends I0> before) {
		Objects.requireNonNull(before);
		return (i1, i2) -> apply(before.get(), i1, i2);
	}

	public default IFunction2<I0, I2, O> compose1(Supplier<? extends I1> before) {
		Objects.requireNonNull(before);
		return (i0, i2) -> apply(i0, before.get(), i2);
	}

	public default IFunction2<I0, I1, O> compose2(Supplier<? extends I2> before) {
		Objects.requireNonNull(before);
		return (i0, i1) -> apply(i0, i1, before.get());
	}

	public default IFunction2<I1, I2, O> curry0(I0 input0) {
		return (input1, input2) -> this.apply(input0, input1, input2);
	}

	public default IFunction2<I0, I2, O> curry1(I1 input1) {
		return (input0, input2) -> this.apply(input0, input1, input2);
	}

	public default IFunction2<I0, I1, O> curry2(I2 input2) {
		return (input0, input1) -> this.apply(input0, input1, input2);
	}

	public default <I0L> IFunction3<I0L, I1, I2, O> lift0(IFunction1<I0L, ? extends I0> lift) {
		return (i0, i1, i2) -> {
			final I0 i0l = lift.apply(i0);
			return apply(i0l, i1, i2);
		};
	}

	public default <I1L> IFunction3<I0, I1L, I2, O> lift1(IFunction1<I1L, ? extends I1> lift) {
		return (i0, i1, i2) -> {
			final I1 i1l = lift.apply(i1);
			return apply(i0, i1l, i2);
		};
	}

	public default <I2L> IFunction3<I0, I1, I2L, O> lift2(IFunction1<I2L, ? extends I2> lift) {
		return (i0, i1, i2) -> {
			final I2 i2l = lift.apply(i2);
			return apply(i0, i1, i2l);
		};
	}

	public default IConsumer3<I0, I1, I2> noReturn() {
		return (i0, i1, i2) -> apply(i0, i1, i2);
	}

	public default IFunction3<I0, I1, I2, O> sync(Object lock) {
		if (lock == null) return this;
		return (i0, i1, i2) -> {
			synchronized (lock) {
				return apply(i0, i1, i2);
			}
		};
	}

	public default IConsumer3<I0, I1, I2> toConsumer() {
		return this::apply;
	}
}
