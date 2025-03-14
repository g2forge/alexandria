package com.g2forge.alexandria.adt.associative.map;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

public class SortedMapBuilder<K, V> {
	public static <K, V> Map<K, V> create(K key, V value) {
		return new SortedMapBuilder<>(key, value).build();
	}

	protected final SortedMap<K, V> map = new TreeMap<>();

	public SortedMapBuilder() {}

	public SortedMapBuilder(K key, V value) {
		put(key, value);
	}

	public SortedMap<K, V> build() {
		return map;
	}

	public Function<K, V> get() {
		return build()::get;
	}

	public SortedMapBuilder<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}
}
