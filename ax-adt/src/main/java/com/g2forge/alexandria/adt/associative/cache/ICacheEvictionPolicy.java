package com.g2forge.alexandria.adt.associative.cache;

import java.util.Collection;

import com.g2forge.alexandria.java.adt.identity.IIdentity;

public interface ICacheEvictionPolicy<K> {
	public Collection<K> access(boolean create, IIdentity<? super K> identity, K key);
}
