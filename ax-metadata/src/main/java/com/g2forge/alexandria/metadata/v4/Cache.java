package com.g2forge.alexandria.metadata.v4;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Cache<K, V> {
	protected static final Object NULL = new Object();
	protected final Function<? super K, ? extends V> generator;

	protected final Map<K, Object> map = new WeakHashMap<>();

	public V get(K key) {
		synchronized (this) {
			final Object reference = map.get(key);
			if (reference != null) {
				if (reference == NULL) return null;
				@SuppressWarnings("unchecked")
				final V value = ((Reference<V>) reference).get();
				if (value != null) return value;
			}

			final V retVal = generator.apply(key);
			map.put(key, retVal == null ? NULL : new SoftReference<>(retVal));
			return retVal;
		}
	}
}
