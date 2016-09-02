package com.g2forge.alexandria.java.optional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class AOptional<T> implements IOptional<T> {
	protected final T value;

	protected AOptional() {
		this.value = null;
	}

	protected AOptional(T value) {
		this.value = require(value);
	}

	protected abstract <_T> AOptional<_T> create();

	protected abstract <_T> AOptional<_T> create(_T value);

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof AOptional)) { return false; }

		final AOptional<?> that = (AOptional<?>) obj;
		return Objects.equals(isPresent(), that.isPresent()) && Objects.equals(value, that.value);
	}

	public AOptional<T> filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (!isPresent()) return this;
		else return predicate.test(value) ? this : create();
	}

	public <U> IOptional<U> flatMap(Function<? super T, IOptional<U>> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent()) return create();
		else return Objects.requireNonNull(mapper.apply(value));
	}

	public T get() {
		if (!isPresent()) { throw new NoSuchElementException("No value present"); }
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Boolean.valueOf(isPresent()), value);
	}

	public void ifPresent(Consumer<? super T> consumer) {
		if (isPresent()) consumer.accept(value);
	}

	public abstract boolean isPresent();

	public <U> AOptional<U> map(Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent()) return create();
		else return create(mapper.apply(value));
	}

	public T orElse(T other) {
		return isPresent() ? value : other;
	}

	public T orElseGet(Supplier<? extends T> other) {
		return isPresent() ? value : other.get();
	}

	public <X extends Throwable> T orElseThrow(Supplier<? extends X> exception) throws X {
		if (isPresent()) return value;
		else throw exception.get();
	}

	protected <_T> _T require(_T value) {
		return value;
	}

	@Override
	public String toString() {
		final String name = getClass().getSimpleName();
		return isPresent() ? String.format("%s[%s]", name, value) : String.format("%s.empty", name);
	}
}
