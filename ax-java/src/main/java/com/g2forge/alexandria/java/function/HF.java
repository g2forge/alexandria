package com.g2forge.alexandria.java.function;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HF {
	public static <I> IConsumer1<I> of(IConsumer1<I> consumer) {
		return IConsumer1.create(consumer);
	}

	public static <I0, I1> IConsumer2<I0, I1> of(IConsumer2<I0, I1> consumer) {
		return IConsumer2.create(consumer);
	}

	public static <I0, I1, I2> IConsumer3<I0, I1, I2> of(IConsumer3<I0, I1, I2> consumer) {
		return IConsumer3.create(consumer);
	}

	public static <T> IPredicate1<T> of(IPredicate1<T> predicate) {
		return IPredicate1.create(predicate);
	}

	public static <I0, I1> IPredicate2<I0, I1> of(IPredicate2<I0, I1> predicate) {
		return IPredicate2.create(predicate);
	}

	public static <I0, I1, I2> IPredicate3<I0, I1, I2> of(IPredicate3<I0, I1, I2> predicate) {
		return IPredicate3.create(predicate);
	}

	public static <I, O, T extends Throwable> IThrowFunction1<I, O, T> of(IThrowFunction1<I, O, T> function) {
		return IThrowFunction1.create(function);
	}

	public static <O, T extends Throwable> IThrowSupplier<O, T> of(IThrowSupplier<O, T> function) {
		return IThrowSupplier.create(function);
	}

	public static <I, O> IFunction1<I, O> of(IFunction1<I, O> function) {
		return IFunction1.create(function);
	}

	public static <I0, I1, O> IFunction2<I0, I1, O> of(IFunction2<I0, I1, O> function) {
		return IFunction2.create(function);
	}

	public static <I0, I1, I2, O> IFunction3<I0, I1, I2, O> of(IFunction3<I0, I1, I2, O> function) {
		return IFunction3.create(function);
	}

	public static IRunnable of(IRunnable runnable) {
		return IRunnable.create(runnable);
	}

	public static <T> ISupplier<T> of(ISupplier<T> supplier) {
		return ISupplier.create(supplier);
	}
}
