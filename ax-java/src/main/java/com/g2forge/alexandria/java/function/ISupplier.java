package com.g2forge.alexandria.java.function;

import java.util.function.Supplier;

@FunctionalInterface
public interface ISupplier<T> extends Supplier<T>, IRunnable {
	public static <T> ISupplier<T> create(ISupplier<T> supplier) {
		return supplier;
	}

	public static <T> ISupplier<T> create(T value) {
		if (value == null) return LiteralSupplier.getNull();
		return new LiteralSupplier<>(value);
	}

	public default void run() {
		get();
	}

	public default <I> IFunction1<I, T> toFunction() {
		return t -> get();
	}

	public default ISupplier<T> wrap(IRunnable pre, IRunnable post) {
		return () -> {
			pre.run();
			try {
				return get();
			} finally {
				post.run();
			}
		};
	}
}
