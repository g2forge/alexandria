package com.g2forge.alexandria.adt.map;

public interface IMapCollector<K, V> {
	public IMapCollector<K, V> add(K key, V value);
}
