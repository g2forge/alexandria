package com.g2forge.alexandria.generic.java.map.implementations;

import java.util.HashMap;
import java.util.Map;

import com.g2forge.alexandria.generic.java.map.ITypedMap;

public class JAPITypedMap<T> implements ITypedMap<T> {
	protected final Map<Class<?>, Object> map = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <_T extends T> _T map(final Class<_T> type) {
		return (_T) map.get(type);
	}
	
	@SuppressWarnings("unchecked")
	public <_T extends T> _T put(final Class<_T> type, final _T value) {
		return (_T) map.put(type, value);
	}
}
