package com.g2forge.alexandria.service;

import java.util.stream.Stream;

public interface IServiceLoader<S> {
	public Stream<? extends S> load();

	public default <_S extends S> Stream<? extends S> load(Class<_S> subtype) {
		return load().filter(s -> subtype.isInstance(s)).map(s -> {
			@SuppressWarnings("unchecked")
			final _S retVal = (_S) s;
			return retVal;
		});
	}
}
