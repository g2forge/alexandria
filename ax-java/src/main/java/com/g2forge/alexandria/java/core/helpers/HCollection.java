package com.g2forge.alexandria.java.core.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HCollection {
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
	public static <T> List<T> concatenate(final Collection<? extends T>... collections) {
		if (collections == null) return null;
		final List<T> retVal = new ArrayList<>();
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
	public static <T> Collection<T> intersection(final Collection<? extends T>... collections) {
		final Collection<T> temp = new LinkedHashSet<>(collections[0]);
		for (int i = 1; i < collections.length; i++) {
			temp.retainAll(collections[i]);
		}
		return temp;
	}

	@SafeVarargs
	public static <T> Collection<T> difference(final Collection<? extends T> minuend, final T... subtrahend) {
		return difference(minuend, com.g2forge.alexandria.java.core.helpers.HCollection.asList(subtrahend));
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

	public static <T> T getAny(final Iterable<? extends T> iterable) {
		return getFirst(iterable);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> emptyList() {
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> emptySet() {
		return Collections.EMPTY_SET;
	}

	public static <T> T getFirst(final Iterable<? extends T> iterable) {
		return iterable.iterator().next();
	}

	public static <T> T getLast(final Iterable<? extends T> iterable) {
		if (iterable instanceof Deque) return ((Deque<? extends T>) iterable).getLast();
		if (iterable instanceof List) {
			final List<? extends T> list = (List<? extends T>) iterable;
			return list.get(list.size() - 1);
		}

		final Iterator<? extends T> iterator = iterable.iterator();
		if (!iterator.hasNext()) throw new NoSuchElementException();
		T retVal = null;
		while (iterator.hasNext()) {
			retVal = iterator.next();
		}
		return retVal;
	}

	public static <T> int getLast(List<? extends T> list, Predicate<? super T> test) {
		return getLast(list, (position, value) -> test.test(value));
	}

	public static <T> int getLast(List<? extends T> list, BiPredicate<Integer, ? super T> test) {
		for (final ListIterator<? extends T> iterator = list.listIterator(list.size()); iterator.hasPrevious();) {
			final int retVal = iterator.previousIndex();
			if (test.test(retVal, iterator.previous())) return retVal;
		}
		return -1;
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
