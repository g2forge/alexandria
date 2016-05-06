package com.g2forge.alexandria.java.associative.implementations;

import java.util.Map;
import java.util.Optional;

import com.g2forge.alexandria.java.associative.IAssociation;

import lombok.Data;

@Data
public class MapAssociation<K, V> implements IAssociation<K, V> {
	protected final Map<K, V> map;

	@Override
	public Optional<V> get(K key, boolean remove) {
		if (!map.containsKey(key)) return Optional.empty();
		return Optional.of(map.get(key));
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
	public Optional<V> set(K key, V value) {
		final boolean contained = map.containsKey(key);
		final V retVal = map.put(key, value);
		return contained ? Optional.empty() : Optional.of(retVal);
	}
}
