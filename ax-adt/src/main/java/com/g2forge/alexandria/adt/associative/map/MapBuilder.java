package com.g2forge.alexandria.adt.associative.map;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class MapBuilder<K, V> {
	public static <K, V> Map<K, V> create(K key, V value) {
		return new MapBuilder<>(key, value).build();
	}
	
	protected final Map<K, V> map = new LinkedHashMap<>();

	public MapBuilder() {}

	public MapBuilder(K key, V value) {
		put(key, value);
	}

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
