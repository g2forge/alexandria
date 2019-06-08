package com.g2forge.alexandria.adt.associative.cache;

import java.util.Collection;

public interface ICacheEvictionPolicy<K> {
	public Collection<K> access(boolean create, K key);
}
