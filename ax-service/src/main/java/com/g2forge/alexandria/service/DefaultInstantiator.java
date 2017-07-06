package com.g2forge.alexandria.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.g2forge.alexandria.java.function.typed.ITypedFunction1;

import lombok.Getter;

public class DefaultInstantiator<S> implements ITypedFunction1<S> {
	@Getter
	protected final Class<?> key;

	@Getter
	protected final Class<S> type;

	protected final Map<String, S> cached = new LinkedHashMap<>();

	public DefaultInstantiator(Class<?> key, Class<S> type) {
		this.key = (key == null) ? type : key;
		this.type = Objects.requireNonNull(type, "You must specify a service type!");
	}

	public DefaultInstantiator(Class<S> type) {
		this(null, type);
	}

	public DefaultInstantiator(IServiceLoader<S> loader) {
		this(loader.getKey(), loader.getType());
	}

	@Override
	public <_S extends S> _S apply(Class<_S> s) {
		final String name = s.getName();
		synchronized (cached) {
			final S cached = this.cached.get(name);
			if (cached != null) return s.cast(cached);

			try {
				final _S retVal = s.cast(getType().cast(instantiate(s)));
				this.cached.put(name, retVal);
				return retVal;
			} catch (Throwable throwable) {
				throw new SmartServiceConfigurationError(getKey(), "Provider \"" + name + "\" could not be instantiated!", throwable);
			}
		}
	}

	protected <_S extends S> _S instantiate(Class<_S> s) throws InstantiationException, IllegalAccessException {
		return s.newInstance();
	}
}