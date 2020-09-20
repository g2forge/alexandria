package com.g2forge.alexandria.java.core.error;

import com.g2forge.alexandria.java.fluent.optional.AOptional;
import com.g2forge.alexandria.java.fluent.optional.AValueOptional;
import com.g2forge.alexandria.java.fluent.optional.NullableOptional;

import lombok.Getter;

public class OrThrowable<T> extends AValueOptional<T> {
	@Getter
	protected final Throwable throwable;

	public OrThrowable(T value) {
		super(value);
		this.throwable = null;
	}

	public OrThrowable(Throwable throwable) {
		super();
		if (throwable == null) throw new NullPointerException();
		this.throwable = throwable;
	}

	@Override
	protected <U> AOptional<U> create() {
		return NullableOptional.empty();
	}

	@Override
	protected <U> AOptional<U> create(U value) {
		return new OrThrowable<U>(value);
	}

	@Override
	public T get() {
		final Throwable throwable = getThrowable();
		if (throwable != null) HError.throwQuietly(throwable);
		return value;
	}

	@Override
	public boolean isEmpty() {
		return getThrowable() != null;
	}
}
