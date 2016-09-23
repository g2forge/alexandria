package com.g2forge.alexandria.java.fluent.optional;

import java.util.Objects;

import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;

public class NonNullOptional<T> extends AOptional<T> {
	protected static final NonNullOptional<?> EMPTY = new NonNullOptional<>();

	public static final IOptionalFactory FACTORY = new IOptionalFactory() {
		@Override
		public <T> NonNullOptional<T> empty() {
			return NonNullOptional.empty();
		}

		@Override
		public <T> NonNullOptional<T> of(T value) {
			return NonNullOptional.of(value);
		}

		@Override
		public <T> NonNullOptional<T> ofNullable(T value) {
			return NonNullOptional.ofNullable(value);
		}
	};

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
	protected <U> AOptional<U> create() {
		return NonNullOptional.empty();
	}

	@Override
	protected <U> AOptional<U> create(U value) {
		return NonNullOptional.of(value);
	}

	@Override
	public boolean isEmpty() {
		return value == null;
	}

	protected <U> U require(U value) {
		return Objects.requireNonNull(value);
	}
}
