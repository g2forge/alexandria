package com.g2forge.alexandria.java.fluent.optional;

import java.util.Objects;
import java.util.Optional;

import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;

/**
 * An optional which cannot ever hold the value <code>null</code>. This class implements roughly the same functionality as {@link java.util.Optional}.
 * 
 * @param <T> The type of the value, if any, held.
 */
public class NonNullOptional<T> extends AValueOptional<T> {
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

	/**
	 * Create a new instance to hold the specified value.
	 * 
	 * @param <T> The type of the value, if any, held.
	 * @param value The non-null value to hold.
	 * @return An optional holding the specified value.
	 * @throws NullPointerException if {@code obj} is {@code null}
	 */
	public static <T> NonNullOptional<T> of(T value) {
		return new NonNullOptional<>(value);
	}

	/**
	 * Create a new instance to hold the specified value, or an empty optional if the specified value is {@code null}.
	 * 
	 * @param <T> The type of the value, if any, held.
	 * @param value The value to hold. If {@code null} the resulting optional will be empty.
	 * @return An optional holding the specified value, or an empty optional.
	 */
	public static <T> NonNullOptional<T> ofNullable(T value) {
		return value == null ? empty() : of(value);
	}

	public static <T> NonNullOptional<T> ofOptional(Optional<? extends T> value) {
		return value.isEmpty() ? empty() : of(value.get());
	}

	protected NonNullOptional() {
		super();
	}

	protected NonNullOptional(T value) {
		super(value);
	}

	@Override
	protected <U> AValueOptional<U> create() {
		return NonNullOptional.empty();
	}

	@Override
	protected <U> AValueOptional<U> create(U value) {
		return NonNullOptional.ofNullable(value);
	}

	@Override
	public boolean isEmpty() {
		return value == null;
	}

	protected <U> U require(U value) {
		return Objects.requireNonNull(value);
	}
}
