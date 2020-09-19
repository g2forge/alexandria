package com.g2forge.alexandria.java.core.error;

import com.g2forge.alexandria.java.adt.tuple.ITuple1G_;

import lombok.Getter;

public class OrThrowable<T> implements ITuple1G_<T> {
	protected final T value;

	@Getter
	protected final Throwable throwable;

	public OrThrowable(T value) {
		this.value = value;
		this.throwable = null;
	}

	public OrThrowable(Throwable throwable) {
		if (throwable == null) throw new NullPointerException();
		this.value = null;
		this.throwable = throwable;
	}

	@Override
	public T get0() {
		final Throwable throwable = getThrowable();
		if (throwable != null) HError.throwQuietly(throwable);
		return value;
	}

	public boolean isValid() {
		return getThrowable() == null;
	}
}
