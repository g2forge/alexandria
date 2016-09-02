package com.g2forge.alexandria.java.optional;

import java.util.Optional;

public interface IOptionalFactory {
	public <T> Optional<T> empty();

	public <T> Optional<T> of(T value);

	public <T> Optional<T> ofNullable(T value);
}
