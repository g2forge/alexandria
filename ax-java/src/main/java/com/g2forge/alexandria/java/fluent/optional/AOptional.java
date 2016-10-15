package com.g2forge.alexandria.java.fluent.optional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.g2forge.alexandria.java.fluent.IFluent1_;

public abstract class AOptional<T> implements IOptional<T> {
	protected final T value;

	protected AOptional() {
		this.value = null;
	}

	protected AOptional(T value) {
		this.value = require(value);
	}

	protected abstract <U> AOptional<U> create();

	protected <U> AOptional<U> create(IFluent1_<U> value) {
		if (getClass().isInstance(value)) return (NonNullOptional<U>) value;
		return value.isEmpty() ? create() : create(value.get());
	}

	protected abstract <U> AOptional<U> create(U value);

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof AOptional)) { return false; }

		final AOptional<?> that = (AOptional<?>) obj;
		return Objects.equals(isEmpty(), that.isEmpty()) && Objects.equals(value, that.value);
	}

	public AOptional<T> filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (isEmpty()) return this;
		else return predicate.test(value) ? this : create();
	}

	public <U> IOptional<U> flatMap(Function<? super T, ? extends IFluent1_<U>> mapper) {
		Objects.requireNonNull(mapper);
		if (isEmpty()) return create();
		else return create(Objects.requireNonNull(mapper.apply(value)));
	}

	public T get() {
		if (isEmpty()) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Boolean.valueOf(isEmpty()), value);
	}

	public <U> AOptional<U> map(Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		if (isEmpty()) return create();
		else return create(mapper.apply(value));
	}

	public T or(T other) {
		return isEmpty() ? other : value;
	}

	public T orGet(Supplier<? extends T> other) {
		return isEmpty() ? other.get() : value;
	}

	public <X extends Throwable> T orThrow(Supplier<? extends X> exception) throws X {
		if (isEmpty()) throw exception.get();
		else return value;
	}

	protected <_T> _T require(_T value) {
		return value;
	}

	@Override
	public String toString() {
		final String name = getClass().getSimpleName();
		return isEmpty() ? String.format("%s.empty", name) : String.format("%s[%s]", name, value);
	}

	public void visit(Consumer<? super T> consumer) {
		if (!isEmpty()) consumer.accept(value);
	}
}
