package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowFunction1<I, O, T extends Throwable> extends IFunction<O>, IThrowConsumer1<I, T> {
	public static <I, O, T extends Throwable> IThrowFunction1<I, O, T> create(IThrowFunction1<I, O, T> function) {
		return function;
	}

	public static <V, T extends Throwable> IThrowFunction1<V, V, T> identity() {
		return v -> v;
	}

	public default void accept(I i) throws T {
		apply(i);
	}

	public O apply(I input) throws T;

	public default IThrowFunction1<I, O, T> sync(Object lock) {
		if (lock == null) return this;
		return i -> {
			synchronized (lock) {
				return apply(i);
			}
		};
	}

	public default IThrowConsumer1<I, T> toConsumer() {
		return this::apply;
	}

	public default <_O> IThrowFunction1<I, _O, T> toFunction(_O retVal) {
		return i -> {
			apply(i);
			return retVal;
		};
	}

	public default IFunction1<I, O> wrap(IFunction1<? super Throwable, ? extends RuntimeException> wrapper) {
		return i -> {
			try {
				return apply(i);
			} catch (Throwable throwable) {
				throw wrapper.apply(throwable);
			}
		};
	}

	public default IThrowFunction1<I, O, T> wrap(IRunnable pre, IRunnable post) {
		return i -> {
			pre.run();
			try {
				return apply(i);
			} finally {
				post.run();
			}
		};
	}
}
