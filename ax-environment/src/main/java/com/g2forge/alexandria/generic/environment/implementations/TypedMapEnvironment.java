package com.g2forge.alexandria.generic.environment.implementations;

import java.util.LinkedHashMap;
import java.util.Map;

import com.g2forge.alexandria.generic.environment.ITypedMapEnvironment;

public class TypedMapEnvironment implements ITypedMapEnvironment {
	protected final Map<Class<?>, Object> map = new LinkedHashMap<>();
	
	@Override
	@SuppressWarnings("unchecked")
	public <_T> _T map(final Class<_T> type) {
		return (_T) map.get(type);
	}
	
	public <_T> void put(final Class<_T> type, final _T value) {
		map.put(type, value);
	}
}
