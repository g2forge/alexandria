package com.g2forge.alexandria.java.fluent.optional;

import java.util.NoSuchElementException;

public abstract class AValueOptional<T> extends AOptional<T> {
	protected final T value;

	protected AValueOptional() {
		this.value = null;
	}

	protected AValueOptional(T value) {
		this.value = require(value);
	}

	public T get() {
		if (isEmpty()) { throw new NoSuchElementException("No value present"); }
		return value;
	}
}
