package com.g2forge.alexandria.java.function;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
public class SequenceSupplier<S, O> implements ISupplier<O> {
	protected S state;

	protected final IFunction1<? super S, ? extends S> next;

	protected final IFunction1<? super S, ? extends O> output;

	@Override
	public O get() {
		final S current = getState();
		setState(getNext().apply(current));
		return getOutput().apply(current);
	}
}
