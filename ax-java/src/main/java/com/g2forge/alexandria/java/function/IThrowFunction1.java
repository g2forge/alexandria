package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowFunction1<I, O, T extends Throwable> {
	public static <I, O, T extends Throwable> IThrowFunction1<I, O, T> create(IThrowFunction1<I, O, T> function) {
		return function;
	}

	public static <V, T extends Throwable> IThrowFunction1<V, V, T> identity() {
		return v -> v;
	}

	public O apply(I input) throws T;

	public default IFunction1<I, O> wrap(IFunction1<? super Throwable, ? extends RuntimeException> wrapper) {
		return i -> {
			try {
				return apply(i);
			} catch (Throwable throwable) {
				throw wrapper.apply(throwable);
			}
		};
	}
}
