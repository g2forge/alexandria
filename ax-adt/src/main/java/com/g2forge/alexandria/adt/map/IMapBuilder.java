package com.g2forge.alexandria.adt.map;

import com.g2forge.alexandria.java.function.ISupplier;

public interface IMapBuilder<M, K, V> extends IMapCollector<K, V>, ISupplier<M> {
	@Override
	public IMapBuilder<M, K, V> add(K key, V value);
}
