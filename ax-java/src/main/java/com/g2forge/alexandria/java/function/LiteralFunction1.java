package com.g2forge.alexandria.java.function;

import java.util.function.Function;

import com.g2forge.alexandria.java.core.helpers.HObject;

import lombok.Data;

@Data
public class LiteralFunction1<I, O> implements IFunction1<I, O> {
	protected static final LiteralFunction1<?, Object> NULL = new LiteralFunction1<>(null);

	@SuppressWarnings("unchecked")
	public static <I, O> IFunction1<I, O> getNull() {
		return (IFunction1<I, O>) NULL;
	}

	public static <O> O unwrap(Function<?, ? extends O> function) {
		if (!(function instanceof LiteralFunction1)) throw new ClassCastException();
		final LiteralFunction1<?, ? extends O> cast = ((LiteralFunction1<?, ? extends O>) function);
		return cast.getValue();
	}

	protected final O value;

	@Override
	public O apply(I input) {
		return getValue();
	}

	@Override
	public String toString() {
		return HObject.toString(this, getValue());
	}
}
