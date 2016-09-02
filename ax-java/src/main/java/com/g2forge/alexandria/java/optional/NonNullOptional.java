package com.g2forge.alexandria.java.optional;

import java.util.Objects;

public class NonNullOptional<T> extends AOptional<T> {
	protected static final NonNullOptional<?> EMPTY = new NonNullOptional<>();

	public static <T> NonNullOptional<T> empty() {
		@SuppressWarnings("unchecked")
		final NonNullOptional<T> retVal = (NonNullOptional<T>) EMPTY;
		return retVal;
	}

	public static <T> NonNullOptional<T> of(T value) {
		return new NonNullOptional<>(value);
	}

	public static <T> NonNullOptional<T> ofNullable(T value) {
		return value == null ? empty() : of(value);
	}

	protected NonNullOptional() {
		super();
	}

	protected NonNullOptional(T value) {
		super(value);
	}

	@Override
	protected <_T> AOptional<_T> create() {
		return NonNullOptional.empty();
	}

	@Override
	protected <_T> AOptional<_T> create(_T value) {
		return NonNullOptional.of(value);
	}

	@Override
	public boolean isPresent() {
		return value != null;
	}

	protected <_T> _T require(_T value) {
		return Objects.requireNonNull(value);
	}
}
