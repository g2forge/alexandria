package com.g2forge.alexandria.java.fluent.optional;

import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;

public class NullableOptional<T> extends AOptional<T> {
	protected static final NullableOptional<?> EMPTY = new NullableOptional<>();

	public static final IOptionalFactory FACTORY = new IOptionalFactory() {
		@Override
		public <T> NullableOptional<T> empty() {
			return NullableOptional.empty();
		}

		@Override
		public <T> NullableOptional<T> of(T value) {
			return NullableOptional.of(value);
		}

		@Override
		public <T> NullableOptional<T> ofNullable(T value) {
			return NullableOptional.ofNullable(value);
		}
	};

	public static <T> NullableOptional<T> empty() {
		@SuppressWarnings("unchecked")
		final NullableOptional<T> retVal = (NullableOptional<T>) EMPTY;
		return retVal;
	}

	public static <T> NullableOptional<T> of(T value) {
		return new NullableOptional<>(value);
	}

	public static <T> NullableOptional<T> ofNullable(T value) {
		return value == null ? empty() : of(value);
	}

	protected final boolean isValid;

	protected NullableOptional() {
		super();
		this.isValid = false;
	}

	protected NullableOptional(T value) {
		super(value);
		this.isValid = true;
	}

	@Override
	protected <_T> AOptional<_T> create() {
		return NullableOptional.empty();
	}

	@Override
	protected <_T> AOptional<_T> create(_T value) {
		return NullableOptional.of(value);
	}

	@Override
	public boolean isEmpty() {
		return !isValid;
	}
}
