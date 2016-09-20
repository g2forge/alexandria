package com.g2forge.alexandria.java.function;

import java.util.function.Supplier;

import com.g2forge.alexandria.java.core.helpers.ObjectHelpers;

import lombok.Data;

@Data
public class LiteralSupplier<T> implements Supplier<T> {
	protected static final LiteralSupplier<Object> NULL = new LiteralSupplier<>(null);

	@SuppressWarnings("unchecked")
	public static <T> Supplier<T> getNull() {
		return (Supplier<T>) NULL;
	}

	public static <T> T unwrap(Supplier<? extends T> supplier) {
		if (!(supplier instanceof LiteralSupplier)) throw new ClassCastException();
		final LiteralSupplier<? extends T> cast = ((LiteralSupplier<? extends T>) supplier);
		return cast.getValue();
	}

	protected final T value;

	@Override
	public T get() {
		return getValue();
	}

	@Override
	public String toString() {
		return ObjectHelpers.toString(this, getValue());
	}
}
