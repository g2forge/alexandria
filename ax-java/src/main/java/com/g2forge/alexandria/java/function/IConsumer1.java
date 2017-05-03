package com.g2forge.alexandria.java.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface IConsumer1<I> extends Consumer<I> {
	public static <I> IConsumer1<I> create(IConsumer1<I> consumer) {
		return consumer;
	}

	@SafeVarargs
	public static <I> IConsumer1<I> fanOut(IConsumer1<I>... consumers) {
		return input -> {
			for (IConsumer1<I> consumer : consumers) {
				consumer.accept(input);
			}
		};
	}

	public static <I> IConsumer1<I> once(IConsumer1<I> consumer) {
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

	public default Runnable curry(I input) {
		return () -> this.accept(input);
	}
}
