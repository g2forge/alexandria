package com.g2forge.alexandria.adt.associative.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.g2forge.alexandria.java.adt.identity.IIdentity;

public class LRUCacheEvictionPolicy<K> implements ICacheEvictionPolicy<K> {
	protected final int size;

	protected final LinkedList<K> keys = new LinkedList<>();

	public LRUCacheEvictionPolicy(int size) {
		this.size = size;
	}

	@Override
	public Collection<K> access(boolean create, IIdentity<? super K> identity, K key) {
		if (!create) {
			if (IIdentity.standard().equals(identity)) keys.remove(key);
			else {
				for (final Iterator<K> iterator = keys.iterator(); iterator.hasNext();) {
					if (identity.equals(key, iterator.next())) {
						iterator.remove();
						break;
					}
				}
			}
		}
		keys.add(key);
		if (keys.size() > size) {
			final List<K> toEvict = keys.subList(0, keys.size() - size);
			final ArrayList<K> retVal = new ArrayList<>(toEvict);
			toEvict.clear();
			return retVal;
		}
		return Collections.emptyList();
	}
}
