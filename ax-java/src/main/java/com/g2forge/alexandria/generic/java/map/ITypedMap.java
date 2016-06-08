package com.g2forge.alexandria.generic.java.map;

public interface ITypedMap<T> {
	public <_T extends T> _T map(Class<_T> type);
}
