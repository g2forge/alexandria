package com.g2forge.alexandria.adt.map.implementations;

import java.util.Map;

import com.g2forge.alexandria.adt.map.IMapBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapMapBuilder<M extends Map<? super K, ? super V>, K, V> implements IMapBuilder<M, K, V> {
	protected final M map;

	@Override
	public IMapBuilder<M, K, V> add(K key, V value) {
		map.put(key, value);
		return this;
	}

	@Override
	public M get() {
		return map;
	}
}
