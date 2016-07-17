package com.g2forge.alexandria.java.associative.map;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapBuilder<K, V> {
	protected final Map<K, V> map = new LinkedHashMap<>();

	public MapBuilder<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}

	public Map<K, V> build() {
		return map;
	}
}
