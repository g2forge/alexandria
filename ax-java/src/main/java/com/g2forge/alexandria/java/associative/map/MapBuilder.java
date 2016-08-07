package com.g2forge.alexandria.java.associative.map;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class MapBuilder<K, V> {
	protected final Map<K, V> map = new LinkedHashMap<>();

	public Map<K, V> build() {
		return map;
	}

	public Function<K, V> get() {
		return build()::get;
	}

	public MapBuilder<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}
}
