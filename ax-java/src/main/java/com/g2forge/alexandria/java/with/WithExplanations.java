package com.g2forge.alexandria.java.with;

import java.util.Collection;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class WithExplanations<T, E> implements IWithExplanation<T, Collection<E>> {
	protected final T value;

	@Singular("explanation")
	protected final List<E> explanation;

	@Override
	public T get() {
		return getValue();
	}
}
