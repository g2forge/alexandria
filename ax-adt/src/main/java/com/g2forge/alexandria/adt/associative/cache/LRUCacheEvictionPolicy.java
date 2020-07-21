package com.g2forge.alexandria.adt.associative.cache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.g2forge.alexandria.java.adt.identity.IIdentity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class LRUCacheEvictionPolicy<K> implements ICacheEvictionPolicy<K> {
	protected final int size;

	protected final LinkedList<K> keys = new LinkedList<>();

	@Override
	public Set<K> access(boolean create, IIdentity<? super K> identity, K key) {
		final LinkedList<K> keys = getKeys();
		if (!create) {
			// If the entry for this key wasn't just created, we need to remove it from the linked list.
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

		// Add the key to the list of keys
		keys.add(key);

		// Get all the keys older than the "size" for eviction, if any
		final int size = getSize();
		if (keys.size() > size) {
			final List<K> toEvict = keys.subList(0, keys.size() - size);
			final Set<K> retVal = new LinkedHashSet<>(toEvict);
			toEvict.clear();
			return retVal;
		}
		return Collections.emptySet();
	}
}
