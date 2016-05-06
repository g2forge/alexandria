package com.g2forge.alexandria.java.associative;

import java.util.Optional;

public interface IAssociation<K, V> {
	public Optional<V> get(K key, boolean remove);

	public void put(K key, V value);

	public void remove(K key);

	public Optional<V> set(K key, V value);
}
