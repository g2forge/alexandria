package com.g2forge.alexandria.java.fluent.optional;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.g2forge.alexandria.java.fluent.IFluent1_;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

public abstract class AOptional<T> implements IOptional<T> {
	@EqualsAndHashCode(callSuper = false)
	@AllArgsConstructor
	protected static class FallbackOptional<T> extends AOptional<T> {
		protected final IOptional<? extends T> override;

		protected final IOptional<? extends T> fallback;

		@Override
		protected <U> AOptional<U> create() {
			return NullableOptional.empty();
		}

		@Override
		protected <U> AOptional<U> create(U value) {
			return NullableOptional.of(value);
		}

		@Override
		public T get() {
			if (!override.isEmpty()) return override.get();
			return fallback.get();
		}

		@Override
		public boolean isEmpty() {
			return override.isEmpty() && fallback.isEmpty();
		}

	}

	protected abstract <U> AOptional<U> create();

	protected <U> AOptional<U> create(IFluent1_<U> value) {
		if (getClass().isInstance(value)) return (AOptional<U>) value;
		return value.isEmpty() ? create() : create(value.get());
	}

	protected abstract <U> AOptional<U> create(U value);

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public IOptional<T> fallback(IOptional<? extends T> fallback) {
		return new FallbackOptional<T>(this, fallback);
	}

	public AOptional<T> filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (isEmpty()) return this;
		else return predicate.test(get()) ? this : create();
	}

	public <U> IOptional<U> flatMap(Function<? super T, ? extends IFluent1_<U>> mapper) {
		Objects.requireNonNull(mapper);
		if (isEmpty()) return create();
		else return create(Objects.requireNonNull(mapper.apply(get())));
	}

	@Override
	public abstract int hashCode();

	public <U> AOptional<U> map(Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		if (isEmpty()) return create();
		else return create(mapper.apply(get()));
	}

	public T or(T other) {
		return isEmpty() ? other : get();
	}

	public T orGet(Supplier<? extends T> other) {
		return isEmpty() ? other.get() : get();
	}

	public <X extends Throwable> T orThrow(Supplier<? extends X> exception) throws X {
		if (isEmpty()) throw exception.get();
		else return get();
	}

	@Override
	public IOptional<T> override(IOptional<? extends T> override) {
		return new FallbackOptional<T>(override, this);
	}

	protected <_T> _T require(_T value) {
		return value;
	}

	@Override
	public String toString() {
		final String name = getClass().getSimpleName();
		return isEmpty() ? String.format("%s.empty", name) : String.format("%s[%s]", name, get());
	}

	@Override
	public void visit(Consumer<? super T> consumer) {
		if (!isEmpty()) consumer.accept(get());
	}
}
