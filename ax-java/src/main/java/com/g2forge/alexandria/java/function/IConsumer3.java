package com.g2forge.alexandria.java.function;

import java.util.Objects;

@FunctionalInterface
public interface IConsumer3<I0, I1, I2> extends IConsumer {
	public static <I0, I1, I2> IConsumer3<I0, I1, I2> ignore() {
		return (i0, i1, i2) -> {};
	}

	public static <I0, I1, I2> IConsumer3<I0, I1, I2> create(IConsumer3<I0, I1, I2> consumer) {
		return consumer;
	}

	public void accept(I0 i0, I1 i1, I2 i2);

	public default IConsumer3<I0, I1, I2> andThen(IConsumer3<? super I0, ? super I1, ? super I2> after) {
		Objects.requireNonNull(after);

		return (i0, i1, i2) -> {
			accept(i0, i1, i2);
			after.accept(i0, i1, i2);
		};
	}

	public default IConsumer2<I1, I2> curry0(I0 input0) {
		return (input1, input2) -> this.accept(input0, input1, input2);
	}

	public default IConsumer2<I0, I2> curry1(I1 input1) {
		return (input0, input2) -> this.accept(input0, input1, input2);
	}

	public default IConsumer2<I0, I1> curry2(I2 input2) {
		return (input0, input1) -> this.accept(input0, input1, input2);
	}

	public default <I0L> IConsumer3<I0L, I1, I2> lift0(IFunction1<I0L, ? extends I0> lift) {
		return (i0, i1, i2) -> {
			final I0 i0l = lift.apply(i0);
			accept(i0l, i1, i2);
		};
	}

	public default <I1L> IConsumer3<I0, I1L, I2> lift1(IFunction1<I1L, ? extends I1> lift) {
		return (i0, i1, i2) -> {
			final I1 i1l = lift.apply(i1);
			accept(i0, i1l, i2);
		};
	}

	public default <I2L> IConsumer3<I0, I1, I2L> lift2(IFunction1<I2L, ? extends I2> lift) {
		return (i0, i1, i2) -> {
			final I2 i2l = lift.apply(i2);
			accept(i0, i1, i2l);
		};
	}

	public default IConsumer3<I0, I1, I2> sync(Object lock) {
		if (lock == null) return this;
		return (i0, i1, i2) -> {
			synchronized (lock) {
				accept(i0, i1, i2);
			}
		};
	}

	public default <O> IFunction3<I0, I1, I2, O> toFunction(O retVal) {
		return (i0, i1, i2) -> {
			accept(i0, i1, i2);
			return retVal;
		};
	}

	public default IConsumer3<I0, I1, I2> wrap(IRunnable pre, IRunnable post) {
		return (i0, i1, i2) -> {
			pre.run();
			try {
				accept(i0, i1, i2);
			} finally {
				post.run();
			}
		};
	}
}
