package com.g2forge.alexandria.adt.associative.cache;

import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.function.Function;

import com.g2forge.alexandria.adt.associative.IAssociation;
import com.g2forge.alexandria.adt.associative.MapAssociation;
import com.g2forge.alexandria.java.adt.identity.IIdentity;
import com.g2forge.alexandria.java.adt.identity.Identified;
import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.Data;

@Data
public class Cache<K, V> implements IFunction1<K, V> {
	protected final IIdentity<? super K> identity;

	protected final Function<? super K, ? extends V> function;

	protected final ICacheEvictionPolicy<K> policy;

	protected final IAssociation<Object, V> cache;

	public Cache(Function<? super K, ? extends V> function, ICacheEvictionPolicy<K> policy) {
		this(null, function, policy, false);
	}

	public Cache(IIdentity<? super K> identity, Function<? super K, ? extends V> function, ICacheEvictionPolicy<K> policy, boolean weak) {
		this.identity = identity == null ? IIdentity.standard() : identity;
		this.function = function;
		this.policy = policy;
		this.cache = new MapAssociation<Object, V>(weak ? new WeakHashMap<>() : new HashMap<>());
	}

	@Override
	public V apply(K key) {
		final Identified<? super K> identified = getIdentity().of(key);
		final IOptional<V> optional = cache.get(identified, false);
		if (!optional.isEmpty()) {
			policy.access(false, getIdentity(), key).stream().map(getIdentity()::of).forEach(cache::remove);
			return optional.get();
		}

		final V retVal = function.apply(key);
		policy.access(true, getIdentity(), key).stream().map(getIdentity()::of).forEach(cache::remove);
		cache.put(identified, retVal);
		return retVal;
	}
}
