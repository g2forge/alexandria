package com.g2forge.alexandria.java.fluent.optional;

import java.util.NoSuchElementException;
import java.util.Objects;

public abstract class AValueOptional<T> extends AOptional<T> {
	protected final T value;

	protected AValueOptional() {
		this.value = null;
	}

	protected AValueOptional(T value) {
		this.value = require(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof AValueOptional)) { return false; }

		final AValueOptional<?> that = (AValueOptional<?>) obj;
		return Objects.equals(isEmpty(), that.isEmpty()) && Objects.equals(value, that.value);
	}

	public T get() {
		if (isEmpty()) { throw new NoSuchElementException("No value present"); }
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Boolean.valueOf(isEmpty()), value);
	}
}
