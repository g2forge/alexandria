package com.g2forge.alexandria.java.function.typed;

import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class MapTypedFunction1<T> implements ITypedFunction1<T> {
	protected final Map<Class<?>, Object> map = new HashMap<>();

	@SuppressWarnings("unchecked")
	@Override
	public <_T extends T> _T apply(final Class<_T> type) {
		return (_T) map.get(type);
	}

	public boolean containsType(Class<?> type) {
		return map.containsKey(type);
	}

	@SuppressWarnings("unchecked")
	public <_T extends T> _T put(final Class<_T> type, final _T value) {
		return (_T) map.put(type, value);
	}

	public void remove(Class<?> type) {
		map.remove(type);
	}
}
