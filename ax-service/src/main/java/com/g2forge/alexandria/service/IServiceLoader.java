package com.g2forge.alexandria.service;

import java.util.Iterator;

import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.java.core.helpers.HStream;

public interface IServiceLoader<S> {
	public ICollection<? extends Class<? extends S>> find();

	public default <_S extends S> ICollection<? extends Class<? extends _S>> find(Class<_S> subtype) {
		return () -> {
			@SuppressWarnings("unchecked")
			final Iterator<Class<? extends _S>> cast = (Iterator<Class<? extends _S>>) find().stream().filter(s -> subtype.isAssignableFrom(s)).iterator();
			return cast;
		};
	}

	public Class<?> getKey();

	public Class<S> getType();

	public ICollection<? extends S> load();

	public default <_S extends S> ICollection<_S> load(Class<_S> subtype) {
		return () -> {
			final Iterator<_S> iterator = HStream.subtype(load().stream(), subtype).iterator();
			return iterator;
		};
	}
}
