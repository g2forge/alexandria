package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowFunction2<I0, I1, O, T extends Throwable> extends IFunction<O>, IThrowConsumer2<I0, I1, T> {
	public static <I0, I1, O, T extends Throwable> IThrowFunction2<I0, I1, O, T> create(IThrowFunction2<I0, I1, O, T> function) {
		return function;
	}

	public default void accept(I0 i0, I1 i1) throws T {
		apply(i0, i1);
	}

	public O apply(I0 input0, I1 input1) throws T;

	public default IThrowFunction2<I0, I1, O, T> sync(Object lock) {
		if (lock == null) return this;
		return (i0, i1) -> {
			synchronized (lock) {
				return apply(i0, i1);
			}
		};
	}

	public default IThrowConsumer2<I0, I1, T> toConsumer() {
		return this::apply;
	}

	public default <_O> IThrowFunction2<I0, I1, _O, T> toFunction(_O retVal) {
		return (i0, i1) -> {
			apply(i0, i1);
			return retVal;
		};
	}

	public default IFunction2<I0, I1, O> wrap(IFunction1<? super Throwable, ? extends RuntimeException> wrapper) {
		return (i0, i1) -> {
			try {
				return apply(i0, i1);
			} catch (Throwable throwable) {
				throw wrapper.apply(throwable);
			}
		};
	}
}
