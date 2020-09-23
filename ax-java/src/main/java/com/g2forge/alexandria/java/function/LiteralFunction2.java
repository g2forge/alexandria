package com.g2forge.alexandria.java.function;

import com.g2forge.alexandria.java.core.helpers.HObject;

import lombok.Data;

@Data
public class LiteralFunction2<I0, I1, O> implements IFunction2<I0, I1, O> {
	protected static final LiteralFunction2<?, ?, Object> NULL = new LiteralFunction2<>(null);

	@SuppressWarnings("unchecked")
	public static <I0, I1, O> IFunction2<I0, I1, O> getNull() {
		return (IFunction2<I0, I1, O>) NULL;
	}

	protected final O value;

	@Override
	public O apply(I0 input0, I1 input1) {
		return getValue();
	}

	@Override
	public String toString() {
		return HObject.toString(this, getValue());
	}
}
