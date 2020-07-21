package com.g2forge.alexandria.adt.associative.cache;

import java.util.Collections;
import java.util.Set;

import com.g2forge.alexandria.java.adt.identity.IIdentity;
import com.g2forge.alexandria.java.core.marker.ISingleton;

public class NeverCacheEvictionPolicy<K> implements ISingleton, ICacheEvictionPolicy<K> {
	protected static final NeverCacheEvictionPolicy<?> singleton = new NeverCacheEvictionPolicy<>();

	@SuppressWarnings("unchecked")
	public static <K> NeverCacheEvictionPolicy<K> create() {
		return (NeverCacheEvictionPolicy<K>) singleton;
	}

	private NeverCacheEvictionPolicy() {}

	@Override
	public Set<K> access(boolean create, IIdentity<? super K> identity, K key) {
		return Collections.emptySet();
	}
}
