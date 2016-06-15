package com.g2forge.alexandria.java.core.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
	public static <T> List<T> asList(T... elements) {
		return Arrays.asList(elements);
	}

	@SafeVarargs
	public static <T> List<T> asListNonNull(T... elements) {
		final List<T> retVal = new ArrayList<>(elements.length);
		for (T element : elements) {
			if (element != null) retVal.add(element);
		}
		return retVal;
	}

	@SafeVarargs
	public static <T> Set<T> asSet(T... elements) {
		return new LinkedHashSet<>(asList(elements));
	}

	@SafeVarargs
	public static <T> Collection<T> concatenate(final Collection<? extends T>... collections) {
		if (collections == null) return null;
		final Collection<T> retVal = new ArrayList<>();
		for (Collection<? extends T> collection : collections) {
			if (collection != null) retVal.addAll(collection);
		}
		return retVal;
	}

	public static <T> Collection<T> difference(final Collection<? extends T> minuend, final Collection<? extends T> subtrahend) {
		final Collection<T> temp = new LinkedHashSet<>(minuend);
		temp.removeAll(subtrahend);
		return temp;
	}

	@SafeVarargs
	public static <T> Collection<T> difference(final Collection<? extends T> minuend, final T... subtrahend) {
		return difference(minuend, com.g2forge.alexandria.java.core.helpers.CollectionHelpers.asList(subtrahend));
	}

	public static <T> Collection<T> filter(final Collection<? extends T> collection, final Predicate<? super T> predicate) {
		return collection.stream().filter(predicate).collect(Collectors.toList());
	}

	public static <T> T get(final Iterable<? extends T> collection, int index) {
		if (index >= 0) {
			if (collection instanceof List) {
				final List<? extends T> list = (List<? extends T>) collection;
				return list.get(index);
			}

			final Iterator<? extends T> iterator = collection.iterator();
			while (index-- > 0)
				iterator.next();
			return iterator.next();
		} else {
			if (collection instanceof List) {
				final List<? extends T> list = (List<? extends T>) collection;
				return list.get(list.size() + index);
			}

			final Object[] history = new Object[-index];
			int i = 0;
			for (final T element : collection) {
				history[i] = element;
				i = (i + 1) % history.length;
			}

			@SuppressWarnings("unchecked")
			final T retVal = (T) history[(i + index + history.length) % history.length];
			return retVal;
		}
	}

	public static <T> T getAny(final Iterable<? extends T> collection) {
		return collection.iterator().next();
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

	public static <I, O> Collection<O> map(final Function<? super I, ? extends O> map, final Collection<? extends I> input) {
		if (input == null) return null;
		return input.stream().map(map).collect(Collectors.toList());
	}

	public static <T> T removeAny(final Collection<T> collection) {
		final Iterator<T> iterator = collection.iterator();
		final T retVal = iterator.next();
		iterator.remove();
		return retVal;
	}

	public static <T> Collection<T> toCollection(final Iterable<T> iterable) {
		if (iterable == null) return null;
		if (iterable instanceof Collection) return (Collection<T>) iterable;
		return toList(iterable);
	}

	public static <T> List<T> toList(final Iterable<T> iterable) {
		if (iterable == null) return null;
		if (iterable instanceof List) return (List<T>) iterable;
		final List<T> retVal = new ArrayList<T>();
		for (final T value : iterable) {
			retVal.add(value);
		}
		return retVal;
	}
}
