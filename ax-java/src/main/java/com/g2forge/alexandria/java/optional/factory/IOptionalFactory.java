package com.g2forge.alexandria.java.optional.factory;

import com.g2forge.alexandria.java.optional.IOptional;

public interface IOptionalFactory {
	public <T> IOptional<T> empty();

	public <T> IOptional<T> of(T value);

	public <T> IOptional<T> ofNullable(T value);

	public default <T> IOptional<T> upcast(IOptional<? extends T> optional) {
		return optional.isPresent() ? of(optional.get()) : empty();
	}
}
