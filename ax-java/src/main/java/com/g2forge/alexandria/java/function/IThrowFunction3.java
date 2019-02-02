package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowFunction3<I0, I1, I2, O, T extends Throwable> extends IFunction<O> {
	public static <I0, I1, I2, O, T extends Throwable> IThrowFunction3<I0, I1, I2, O, T> create(IThrowFunction3<I0, I1, I2, O, T> function) {
		return function;
	}

	public O apply(I0 input0, I1 input1, I2 input2) throws T;

	public default IFunction3<I0, I1, I2, O> wrap(IFunction1<? super Throwable, ? extends RuntimeException> wrapper) {
		return (i0, i1, i2) -> {
			try {
				return apply(i0, i1, i2);
			} catch (Throwable throwable) {
				throw wrapper.apply(throwable);
			}
		};
	}
}
