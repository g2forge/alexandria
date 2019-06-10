package com.g2forge.alexandria.adt.associative;

import com.g2forge.alexandria.java.fluent.optional.IOptional;

public interface IAssociation<K, V> {
	public IOptional<V> get(K key, boolean remove);

	public void put(K key, V value);

	public void remove(K key);

	public IOptional<V> set(K key, V value);
}
