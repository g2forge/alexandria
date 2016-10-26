package com.g2forge.alexandria.java.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface IConsumer<I> extends Consumer<I> {
	public static <I> IConsumer<I> create(IConsumer<I> consumer) {
		return consumer;
	}

	public default Runnable curry(I input) {
		return () -> this.accept(input);
	}
}
