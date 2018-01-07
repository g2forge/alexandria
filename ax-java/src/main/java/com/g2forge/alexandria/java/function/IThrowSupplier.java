package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowSupplier<O, T extends Throwable> {
	public static <O, T extends Throwable> IThrowSupplier<O, T> create(IThrowSupplier<O, T> function) {
		return function;
	}

	public O get() throws T;

	public default ISupplier<O> wrap(IFunction1<? super Throwable, ? extends RuntimeException> wrapper) {
		return () -> {
			try {
				return get();
			} catch (Throwable throwable) {
				throw wrapper.apply(throwable);
			}
		};
	}
}
