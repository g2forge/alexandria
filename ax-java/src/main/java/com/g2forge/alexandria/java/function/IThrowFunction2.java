package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowFunction2<I0, I1, O, T extends Throwable> extends IFunction<O> {
	public static <I0, I1, O, T extends Throwable> IThrowFunction2<I0, I1, O, T> create(IThrowFunction2<I0, I1, O, T> function) {
		return function;
	}

	public O apply(I0 input0, I1 input1) throws T;

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
