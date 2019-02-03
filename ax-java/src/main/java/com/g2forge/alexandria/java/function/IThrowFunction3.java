package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowFunction3<I0, I1, I2, O, T extends Throwable> extends IFunction<O>, IThrowConsumer3<I0, I1, I2, T> {
	public static <I0, I1, I2, O, T extends Throwable> IThrowFunction3<I0, I1, I2, O, T> create(IThrowFunction3<I0, I1, I2, O, T> function) {
		return function;
	}

	public default void accept(I0 i0, I1 i1, I2 i2) throws T {
		apply(i0, i1, i2);
	}

	public O apply(I0 input0, I1 input1, I2 input2) throws T;

	public default IThrowFunction3<I0, I1, I2, O, T> sync(Object lock) {
		if (lock == null) return this;
		return (i0, i1, i2) -> {
			synchronized (lock) {
				return apply(i0, i1, i2);
			}
		};
	}

	public default IThrowConsumer3<I0, I1, I2, T> toConsumer() {
		return this::apply;
	}

	public default <_O> IThrowFunction3<I0, I1, I2, _O, T> toFunction(_O retVal) {
		return (i0, i1, i2) -> {
			apply(i0, i1, i2);
			return retVal;
		};
	}

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
