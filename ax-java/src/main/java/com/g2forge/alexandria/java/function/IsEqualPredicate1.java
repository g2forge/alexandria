package com.g2forge.alexandria.java.function;

import java.util.Objects;

import com.g2forge.alexandria.java.core.helpers.HObject;

import lombok.Data;

@Data
public class IsEqualPredicate1<T> implements IPredicate1<T> {
	public static <T> T unwrap(IPredicate1<? extends T> predicate) {
		if (!(predicate instanceof IsEqualPredicate1)) throw new ClassCastException();
		final IsEqualPredicate1<? extends T> cast = ((IsEqualPredicate1<? extends T>) predicate);
		return cast.getValue();
	}

	protected final T value;

	@Override
	public boolean test(T t) {
		return Objects.equals(getValue(), t);
	}

	@Override
	public String toString() {
		return HObject.toString(this, getValue());
	}
}
