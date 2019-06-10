package com.g2forge.alexandria.adt.associative;

import java.util.Map;

import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.NullableOptional;

import lombok.Data;

@Data
public class MapAssociation<K, V> implements IAssociation<K, V> {
	protected final Map<K, V> map;

	@Override
	public IOptional<V> get(K key, boolean remove) {
		if (!map.containsKey(key)) return NullableOptional.empty();
		return NullableOptional.of(map.get(key));
	}

	@Override
	public void put(K key, V value) {
		map.put(key, value);
	}

	@Override
	public void remove(K key) {
		map.remove(key);
	}

	@Override
	public IOptional<V> set(K key, V value) {
		final boolean contained = map.containsKey(key);
		final V retVal = map.put(key, value);
		return contained ? NullableOptional.empty() : NullableOptional.of(retVal);
	}
}
