package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface IConsumer2<I0, I1> extends BiConsumer<I0, I1>, IConsumer {
	public static <I0, I1> IConsumer2<I0, I1> create(IConsumer2<I0, I1> consumer) {
		return consumer;
	}
	
	public static <I0, I1> IConsumer2<I0, I1> ignore() {
		return (i0, i1) -> {};
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

	public default <I0L> IConsumer2<I0L, I1> lift0(IFunction1<I0L, ? extends I0> lift) {
		return (i0, i1) -> {
			final I0 i0l = lift.apply(i0);
			accept(i0l, i1);
		};
	}

	public default <I1L> IConsumer2<I0, I1L> lift1(IFunction1<I1L, ? extends I1> lift) {
		return (i0, i1) -> {
			final I1 i1l = lift.apply(i1);
			accept(i0, i1l);
		};
	}

	public default IConsumer2<I0, I1> sync(Object lock) {
		if (lock == null) return this;
		return (i0, i1) -> {
			synchronized (lock) {
				accept(i0, i1);
			}
		};
	}
	
	public default <O> IFunction2<I0, I1, O> toFunction(O retVal) {
		return (i0, i1) -> {
			accept(i0, i1);
			return retVal;
		};
	}
	
	public default IConsumer2<I0, I1> wrap(IRunnable pre, IRunnable post) {
		return (i0, i1) -> {
			pre.run();
			try {
				accept(i0, i1);
			} finally {
				post.run();
			}
		};
	}
}
