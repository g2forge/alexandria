package com.g2forge.alexandria.adt.map;

import com.g2forge.alexandria.java.core.iface.IFactory;

public interface IMapBuilder<M, K, V> extends IMapCollector<K, V>, IFactory<M> {
	@Override
	public IMapBuilder<M, K, V> add(K key, V value);
}
