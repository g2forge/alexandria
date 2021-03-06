package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface IConsumer1<I> extends Consumer<I>, IConsumer, IThrowConsumer1<I, RuntimeException> {
	public static <I> IConsumer1<I> create(IConsumer1<I> consumer) {
		return consumer;
	}

	@SafeVarargs
	public static <I> IConsumer1<I> fanOut(IConsumer1<? super I>... consumers) {
		for (IConsumer1<? super I> consumer : consumers)
			Objects.requireNonNull(consumer);
		return input -> {
			for (IConsumer1<? super I> consumer : consumers) {
				consumer.accept(input);
			}
		};
	}

	public static <I> IConsumer1<I> ignore() {
		return i -> {};
	}

	public static <I> IConsumer1<I> once(IConsumer1<I> consumer) {
		Objects.requireNonNull(consumer);
		return new IConsumer1<I>() {
			protected boolean called = false;

			@Override
			public void accept(I input) {
				if (called) throw new IllegalStateException("Can only be called once!");
				try {
					consumer.accept(input);
				} finally {
					called = true;
				}
			}
		};
	}

	public default IConsumer1<I> andThen(Consumer<? super I> after) {
		Objects.requireNonNull(after);
		return (I input) -> {
			accept(input);
			after.accept(input);
		};
	}

	public default IRunnable curry(I input) {
		return () -> this.accept(input);
	}

	public default <IL> IConsumer1<IL> lift(IFunction1<IL, ? extends I> lift) {
		return il -> accept(lift.apply(il));
	}

	public default IConsumer1<I> sync(Object lock) {
		if (lock == null) return this;
		return i -> {
			synchronized (lock) {
				accept(i);
			}
		};
	}

	public default <O> IFunction1<I, O> toFunction(O retVal) {
		return i -> {
			accept(i);
			return retVal;
		};
	}

	public default IConsumer1<I> wrap(IRunnable pre, IRunnable post) {
		return i -> {
			pre.run();
			try {
				accept(i);
			} finally {
				post.run();
			}
		};
	}
}
