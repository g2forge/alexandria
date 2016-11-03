package com.g2forge.alexandria.java.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface IConsumer1<I> extends Consumer<I> {
	public static <I> IConsumer1<I> create(IConsumer1<I> consumer) {
		return consumer;
	}

	public default Runnable curry(I input) {
		return () -> this.accept(input);
	}
}
