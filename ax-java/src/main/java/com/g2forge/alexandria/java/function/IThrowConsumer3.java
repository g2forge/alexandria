package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowConsumer3<I0, I1, I2, T extends Throwable> extends IConsumer {
	public static <I0, I1, I2, T extends Throwable> IThrowConsumer3<I0, I1, I2, T> create(IThrowConsumer3<I0, I1, I2, T> function) {
		return function;
	}

	public void accept(I0 input0, I1 input1, I2 input2) throws T;

	public default IThrowConsumer3<I0, I1, I2, T> sync(Object lock) {
		if (lock == null) return this;
		return (i0, i1, i2) -> {
			synchronized (lock) {
				accept(i0, i1, i2);
			}
		};
	}

	public default <O> IThrowFunction3<I0, I1, I2, O, T> toFunction(O retVal) {
		return (i0, i1, i2) -> {
			accept(i0, i1, i2);
			return retVal;
		};
	}
	
	public default IConsumer3<I0, I1, I2> wrap(IFunction1<? super Throwable, ? extends RuntimeException> wrapper) {
		return (i0, i1, i2) -> {
			try {
				accept(i0, i1, i2);
			} catch (Throwable throwable) {
				throw wrapper.apply(throwable);
			}
		};
	}
}
