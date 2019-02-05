package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowConsumer2<I0, I1, T extends Throwable> extends IConsumer {
	public static <I0, I1, T extends Throwable> IThrowConsumer2<I0, I1, T> create(IThrowConsumer2<I0, I1, T> function) {
		return function;
	}

	public void accept(I0 input0, I1 input1) throws T;

	public default IThrowConsumer2<I0, I1, T> sync(Object lock) {
		if (lock == null) return this;
		return (i0, i1) -> {
			synchronized (lock) {
				accept(i0, i1);
			}
		};
	}

	public default <O> IThrowFunction2<I0, I1, O, T> toFunction(O retVal) {
		return (i0, i1) -> {
			accept(i0, i1);
			return retVal;
		};
	}

	public default IConsumer2<I0, I1> wrap(IFunction1<? super Throwable, ? extends RuntimeException> wrapper) {
		return (i0, i1) -> {
			try {
				accept(i0, i1);
			} catch (Throwable throwable) {
				throw wrapper.apply(throwable);
			}
		};
	}

	public default IThrowConsumer2<I0, I1, T> wrap(IRunnable pre, IRunnable post) {
		return (i0, i1) -> {
			pre.run();
			try {
				accept(i0, i1);
			} finally {
				post.run();
			}
		};
	}
}
