package com.g2forge.alexandria.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CollectionHelpers {
	public static <K, C extends Collection<V>, V> void add(Map<K, C> map, Supplier<? extends C> constructor, K key, V value) {
		final C collection;
		if (!map.containsKey(key)) map.put(key, collection = constructor.get());
		else collection = map.get(key);
		collection.add(value);
	}

	public static <T> List<T> asList(Collection<T> collection) {
		if (collection instanceof List) return (List<T>) collection;
		return new ArrayList<>(collection);
	}

	@SafeVarargs
	public static <T> List<T> asListNonNull(T... elements) {
		final List<T> retVal = new ArrayList<>(elements.length);
		for (T element : elements) {
			if (element != null) retVal.add(element);
		}
		return retVal;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getEmpty() {
		return Collections.EMPTY_LIST;
	}

	public static <T> T getOne(final Iterable<? extends T> collection) {
		final Iterator<? extends T> iterator = collection.iterator();
		final T retVal = iterator.next();
		if (iterator.hasNext()) throw new IllegalArgumentException("Input collection had more than one element!");
		return retVal;
	}
}
