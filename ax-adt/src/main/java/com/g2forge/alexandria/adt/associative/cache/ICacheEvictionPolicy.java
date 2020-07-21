package com.g2forge.alexandria.adt.associative.cache;

import java.util.Set;

import com.g2forge.alexandria.java.adt.identity.IIdentity;

public interface ICacheEvictionPolicy<K> {
	/**
	 * Record an access to the specified key, and get the list of keys to evict as a result.
	 * 
	 * @param create {@code true} if the {@code key} was just created.
	 * @param identity The identity of the {@code key} for comparison purposes.
	 * @param key The key which was just accessed.
	 * @return A set of keys to evict from the cache.
	 */
	public Set<K> access(boolean create, IIdentity<? super K> identity, K key);
}
