package com.g2forge.alexandria.java.function;

import java.util.function.Supplier;

import com.g2forge.alexandria.java.core.helpers.HObject;

import lombok.Data;

@Data
public class LiteralSupplier<T> implements ISupplier<T> {
	protected static final LiteralSupplier<Object> NULL = new LiteralSupplier<>(null);

	@SuppressWarnings("unchecked")
	public static <T> ISupplier<T> getNull() {
		return (ISupplier<T>) NULL;
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
		return HObject.toString(this, getValue());
	}
}
