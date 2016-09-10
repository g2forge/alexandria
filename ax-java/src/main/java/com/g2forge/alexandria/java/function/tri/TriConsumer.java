package com.g2forge.alexandria.java.function.tri;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T, U, V> {
	public void accept(T t, U u, V v);

	public default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
		Objects.requireNonNull(after);

		return (t, u, v) -> {
			accept(t, u, v);
			after.accept(t, u, v);
		};
	}
}