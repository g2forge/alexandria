package com.g2forge.alexandria.java.fluent.optional;

import java.util.Optional;

import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;

/**
 * An optional which holds a value which may be {@code null} without being empty. This class differs from {@link java.util.Optional} and {@link NonNullOptional}
 * in that {@code null} is not the same as empty, {@code null} is a valid value which this optional may hold.
 * 
 * @param <T> The type of the value, if any, held.
 */
public class NullableOptional<T> extends AValueOptional<T> {
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

	/**
	 * Create a new instance to hold the specified value.
	 * 
	 * @param <T> The type of the value, if any, held.
	 * @param value The null value to hold.
	 * @return An optional holding the specified value.
	 */
	public static <T> NullableOptional<T> of(T value) {
		return new NullableOptional<>(value);
	}

	/**
	 * Create a new instance to hold the specified value, or an empty optional if the specified value is {@code null}.
	 * 
	 * @param <T> The type of the value, if any, held.
	 * @param value The value to hold. If {@code null} the resulting optional will be empty.
	 * @return An optional holding the specified value, or an empty optional.
	 */
	public static <T> NullableOptional<T> ofNullable(T value) {
		return value == null ? empty() : of(value);
	}

	public static <T> NullableOptional<T> ofOptional(Optional<? extends T> value) {
		return value.isEmpty() ? empty() : of(value.get());
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
	protected <_T> AValueOptional<_T> create() {
		return NullableOptional.empty();
	}

	@Override
	protected <_T> AValueOptional<_T> create(_T value) {
		return NullableOptional.of(value);
	}

	@Override
	public boolean isEmpty() {
		return !isValid;
	}
}
