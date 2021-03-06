package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import com.g2forge.alexandria.java.core.marker.ISingleton;

@FunctionalInterface
public interface IFunction1<I, O> extends Function<I, O>, IFunction<O>, IConsumer1<I>, IThrowFunction1<I, O, RuntimeException> {
	public static class Identity<T> implements IFunction1<T, T>, ISingleton {
		protected static Identity<?> INSTANCE = new Identity<>();

		@SuppressWarnings("unchecked")
		public static <T> Identity<T> create() {
			return (Identity<T>) INSTANCE;
		}

		private Identity() {}

		@Override
		public T apply(T t) {
			return t;
		}
	}

	@SuppressWarnings("unchecked")
	public static <I, O> IFunction1<I, O> cast() {
		return i -> (O) i;
	}

	public static <I, O> IFunction1<I, O> create(IFunction1<I, O> function) {
		return function;
	}

	public static <I, O> IFunction1<I, O> create(O constant) {
		if (constant == null) return LiteralFunction1.getNull();
		return new LiteralFunction1<>(constant);
	}

	public static <T> IFunction1<T, T> identity() {
		return Identity.create();
	}

	@SuppressWarnings("unchecked")
	public static <I, O> IFunction1<I, O> isInstanceOf(Class<O> type) {
		return i -> type.isInstance(i) ? (O) i : null;
	}

	public static <I, O> IFunction1<I, O> liftNull(IFunction1<? super I, ? extends O> function) {
		return liftNull(function, null);
	}

	public static <I, O> IFunction1<I, O> liftNull(IFunction1<? super I, ? extends O> function, O other) {
		return i -> {
			if (i == null) return other;
			return function.apply(i);
		};
	}

	public static <T> ToIntFunction<T> liftNull(IFunction1<? super T, Integer> function, int other) {
		return t -> {
			final Integer retVal = function.apply(t);
			if (retVal == null) return other;
			return retVal;
		};
	}

	public default void accept(I i) {
		apply(i);
	}

	public default <X> IFunction1<I, X> andThen(IFunction1<? super O, ? extends X> f) {
		return i -> f.apply(apply(i));
	}

	public default O applyWithFallback(@SuppressWarnings("unchecked") I... inputs) {
		for (I input : inputs) {
			final O retVal = apply(input);
			if (retVal != null) return retVal;
		}
		return null;
	}

	public default Supplier<O> compose(Supplier<? extends I> before) {
		Objects.requireNonNull(before);
		return () -> apply(before.get());
	}

	public default ISupplier<O> curry(I input) {
		return () -> apply(input);
	}

	public default <IL> IFunction1<IL, O> lift(IFunction1<IL, ? extends I> lift) {
		return lift.andThen(this);
	}

	public default <T> IFunction1<I, T> lift(T equal, IFunction1<? super O, ? extends T> lift) {
		return i -> {
			final O o = apply(i);
			if (i == o) return equal;
			return lift.apply(o);
		};
	}

	public default IConsumer1<I> noReturn() {
		return i -> apply(i);
	}

	public default IFunction1<I, O> override(O compare, O output) {
		return i -> {
			final O retVal = apply(i);
			return (retVal == compare) ? output : retVal;
		};
	}

	public default IFunction1<I, O> sync(Object lock) {
		if (lock == null) return this;
		return i -> {
			synchronized (lock) {
				return apply(i);
			}
		};
	}

	public default IConsumer1<I> toConsumer() {
		return this::apply;
	}

	public default <_O> IFunction1<I, _O> toFunction(_O retVal) {
		return i -> {
			apply(i);
			return retVal;
		};
	}

	public default IFunction1<I, O> wrap(IRunnable pre, IRunnable post) {
		return i -> {
			if (pre != null) pre.run();
			try {
				return apply(i);
			} finally {
				if (post != null) post.run();
			}
		};
	}
}
