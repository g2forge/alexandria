package com.g2forge.alexandria.java.associative.cache;

import java.util.Collection;
import java.util.Collections;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class NeverCacheEvictionPolicy<K> implements ISingleton, ICacheEvictionPolicy<K> {
	protected static final NeverCacheEvictionPolicy<?> singleton = new NeverCacheEvictionPolicy<>();

	@SuppressWarnings("unchecked")
	public static <K> NeverCacheEvictionPolicy<K> create() {
		return (NeverCacheEvictionPolicy<K>) singleton;
	}

	private NeverCacheEvictionPolicy() {}

	@Override
	public Collection<K> access(boolean create, K key) {
		return Collections.emptyList();
	}

}
