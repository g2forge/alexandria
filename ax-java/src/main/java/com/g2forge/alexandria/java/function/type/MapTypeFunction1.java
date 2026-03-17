package com.g2forge.alexandria.java.function.type;

import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class MapTypeFunction1<T> implements ITypeFunction1<T> {
	protected final Map<Class<?>, Object> map = new HashMap<>();

	@Override
	public <_T extends T> _T apply(final Class<_T> type) {
		return type.cast(map.get(type));
	}

	public boolean containsType(Class<?> type) {
		return map.containsKey(type);
	}

	public <_T extends T> _T put(final Class<_T> type, final _T value) {
		return type.cast(map.put(type, value));
	}

	public void remove(Class<?> type) {
		map.remove(type);
	}
}
