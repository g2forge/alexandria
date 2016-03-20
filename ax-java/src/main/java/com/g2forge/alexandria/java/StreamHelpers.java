package com.g2forge.alexandria.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamHelpers {
	@SafeVarargs
	public static <T> Stream<T> concat(Stream<T>... streams) {
		return Arrays.stream(streams).reduce(Stream::concat).get();
	}

	public static <T> Collector<T, List<T>, List<T>> interleave(T separator) {
		return Collector.of(ArrayList::new, (list, element) -> {
			if (!list.isEmpty()) list.add(separator);
			list.add(element);
		} , (list0, list1) -> {
			if (!list0.isEmpty()) list0.add(separator);
			list0.addAll(list1);
			return list0;
		});
	}

	public static <I0, I1, O> Stream<O> product(BiFunction<I0, I1, O> aggregator, Supplier<Stream<I0>> stream0, Supplier<Stream<I1>> stream1) {
		return stream0.get().flatMap(v0 -> stream1.get().map(v1 -> aggregator.apply(v0, v1)));
	}

	@SafeVarargs
	public static <T> Stream<T> product(BinaryOperator<T> aggregator, Supplier<Stream<T>>... streams) {
		return Arrays.stream(streams).reduce((s0, s1) -> () -> s0.get().flatMap(v0 -> s1.get().map(v1 -> aggregator.apply(v0, v1)))).orElse(Stream::empty).get();
	}

	public static <T> Collection<T> toList(Iterator<T> iterator, Supplier<? extends Collection<T>> constructor) {
		final Collection<T> retVal = constructor.get();
		iterator.forEachRemaining(retVal::add);
		return retVal;
	}

	public static <T> Collector<T, ?, T> toOne() {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			if (list.size() != 1) throw new IllegalStateException("Result set had " + list.size() + " elements instead of 1!");
			return list.get(0);
		});
	}

	public static <T> Collector<T, ?, Optional<T>> toOptional() {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			if (list.size() > 1) throw new IllegalStateException("Result set had " + list.size() + " elements instead of 0 or 1!");
			if (list.isEmpty()) return Optional.empty();
			return Optional.of(list.get(0));
		});
	}

	public static <T> Stream<T> toStream(Iterator<T> iterator) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
	}
}
