package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowConsumer1<I, T extends Throwable> extends IConsumer {
	public static <I, T extends Throwable> IThrowConsumer1<I, T> create(IThrowConsumer1<I, T> function) {
		return function;
	}
	
	public void accept(I input) throws T;

	public default IThrowConsumer1<I, T> sync(Object lock) {
		if (lock == null) return this;
		return i -> {
			synchronized (lock) {
				accept(i);
			}
		};
	}

	public default <O> IThrowFunction1<I, O, T> toFunction(O retVal) {
		return i -> {
			accept(i);
			return retVal;
		};
	}
	
	public default IConsumer1<I> wrap(IFunction1<? super Throwable, ? extends RuntimeException> wrapper) {
		return i -> {
			try {
				accept(i);
			} catch (Throwable throwable) {
				throw wrapper.apply(throwable);
			}
		};
	}
	
	public default IThrowConsumer1<I, T> wrap(IRunnable pre, IRunnable post) {
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
