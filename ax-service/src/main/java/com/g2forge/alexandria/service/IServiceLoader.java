package com.g2forge.alexandria.service;

import java.util.stream.Stream;

public interface IServiceLoader<S> {
	public default Stream<? extends Class<? extends S>> find() {
		return load().map(s -> {
			@SuppressWarnings("unchecked")
			final Class<? extends S> retVal = (Class<? extends S>) s.getClass();
			return retVal;
		});
	}

	public Class<S> getType();

	public Stream<? extends S> load();

	public default <_S extends S> Stream<? extends S> load(Class<_S> subtype) {
		return load().filter(s -> subtype.isInstance(s)).map(subtype::cast);
	}
}
