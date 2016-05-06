package com.g2forge.alexandria.java.associative.cache;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

import com.g2forge.alexandria.java.associative.IAssociation;
import com.g2forge.alexandria.java.associative.implementations.MapAssociation;

import lombok.Data;

@Data
public class Cache<K, V> implements Function<K, V> {
	protected final Function<? super K, ? extends V> function;

	protected final ICacheEvictionPolicy<K> policy;

	protected final IAssociation<K, V> cache = new MapAssociation<>(new HashMap<>());

	@Override
	public V apply(K key) {
		final Optional<V> optional = cache.get(key, false);
		if (optional.isPresent()) {
			policy.access(false, key).forEach(cache::remove);
			return optional.get();
		}

		final V retVal = function.apply(key);
		policy.access(true, key).forEach(cache::remove);
		cache.put(key, retVal);
		return retVal;
	}
}
