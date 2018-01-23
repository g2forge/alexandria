package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowConsumer1<I, T extends Throwable> extends IConsumer {
	public static <I, T extends Throwable> IThrowConsumer1<I, T> create(IThrowConsumer1<I, T> function) {
		return function;
	}

	public void accept(I input) throws T;

	public default IConsumer1<I> wrap(IFunction1<? super Throwable, ? extends RuntimeException> wrapper) {
		return i -> {
			try {
				accept(i);
			} catch (Throwable throwable) {
				throw wrapper.apply(throwable);
			}
		};
	}
}
