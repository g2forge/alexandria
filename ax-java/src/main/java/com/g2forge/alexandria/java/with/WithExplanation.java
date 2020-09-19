package com.g2forge.alexandria.java.with;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class WithExplanation<T, E> implements IWithExplanation<T, E> {
	protected final T value;

	protected final E explanation;

	@Override
	public T get() {
		return getValue();
	}
}
