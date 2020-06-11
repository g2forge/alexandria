package com.g2forge.alexandria.service;

import java.lang.reflect.InvocationTargetException;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class DefaultInstantiator<S> extends NewInstanceInstantiator<S> {
	public DefaultInstantiator(Class<?> key, Class<S> type) {
		super(key, type);
	}

	public DefaultInstantiator(Class<S> type) {
		super(type);
	}

	public DefaultInstantiator(IServiceLoader<S> loader) {
		this(loader.getKey(), loader.getType());
	}

	protected <_S extends S> _S instantiate(Class<_S> s) throws InstantiationException, IllegalAccessException {
		if (ISingleton.class.isAssignableFrom(s)) {
			try {
				return s.cast(s.getDeclaredMethod("create").invoke(null));
			} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(String.format("Class %1$s does not properly implement the %2$s contract with a static create() method!", s.getName(), ISingleton.class.getSimpleName()),  e);
			}
		}
		return super.instantiate(s);
	}
}