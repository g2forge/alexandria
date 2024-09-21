package com.g2forge.alexandria.service;

import java.util.stream.Stream;

import com.g2forge.alexandria.collection.DStreamCollection;
import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.java.core.helpers.HStream;

public interface IServiceLoader<S> {
	public ICollection<? extends Class<? extends S>> find();

	public default <_S extends S> ICollection<? extends Class<? extends _S>> find(Class<_S> subtype) {
		return (DStreamCollection<? extends Class<? extends _S>>) (() -> {
			@SuppressWarnings("unchecked")
			final Stream<Class<? extends _S>> retVal = (Stream<Class<? extends _S>>) find().stream().filter(s -> subtype.isAssignableFrom(s));
			return retVal;
		});
	}

	public Class<?> getKey();

	public Class<S> getType();

	public ICollection<? extends S> load();

	public default <_S extends S> ICollection<_S> load(Class<_S> subtype) {
		return ((DStreamCollection<_S>) () -> {
			return HStream.subtype(load().stream(), subtype);
		});
	}
}
