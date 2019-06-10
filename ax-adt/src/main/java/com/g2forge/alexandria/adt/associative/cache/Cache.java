package com.g2forge.alexandria.adt.associative.cache;

import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.function.Function;

import com.g2forge.alexandria.adt.associative.IAssociation;
import com.g2forge.alexandria.adt.associative.MapAssociation;
import com.g2forge.alexandria.java.fluent.optional.IOptional;

import lombok.Data;

@Data
public class Cache<K, V> implements Function<K, V> {
	protected final Function<? super K, ? extends V> function;

	protected final ICacheEvictionPolicy<K> policy;

	protected final IAssociation<K, V> cache;

	public Cache(Function<? super K, ? extends V> function, ICacheEvictionPolicy<K> policy) {
		this(function, policy, false);
	}

	public Cache(Function<? super K, ? extends V> function, ICacheEvictionPolicy<K> policy, boolean weak) {
		this.function = function;
		this.policy = policy;
		this.cache = new MapAssociation<>(weak ? new WeakHashMap<>() : new HashMap<>());
	}

	@Override
	public V apply(K key) {
		final IOptional<V> optional = cache.get(key, false);
		if (!optional.isEmpty()) {
			policy.access(false, key).forEach(cache::remove);
			return optional.get();
		}

		final V retVal = function.apply(key);
		policy.access(true, key).forEach(cache::remove);
		cache.put(key, retVal);
		return retVal;
	}
}
