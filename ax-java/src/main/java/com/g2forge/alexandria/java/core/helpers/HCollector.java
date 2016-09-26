package com.g2forge.alexandria.java.core.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.function.IFunction;
import com.g2forge.alexandria.java.marker.Helpers;
import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.implementations.Tuple2G_O;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HCollector {
	public static <T> Collector<T, ?, Optional<T>> toOptional() {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			if (list.size() > 1) throw new IllegalStateException("Result set had " + list.size() + " elements instead of 0 or 1!");
			if (list.isEmpty()) return Optional.empty();
			return Optional.of(list.get(0));
		});
	}

	public static <T> Collector<T, ?, T> toOne() {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			if (list.size() != 1) throw new IllegalStateException("Result set had " + list.size() + " elements instead of 1!");
			return list.get(0);
		});
	}

	public static <K, V> Collector<Tuple2G_O<K, V>, ?, Map<K, V>> toMap() {
		return Collectors.toMap(ITuple2G_::get0, ITuple2G_::get1, (u, v) -> {
			throw new IllegalStateException(String.format("Duplicate key %s", u));
		} , () -> new LinkedHashMap<>());
	}

	public static <T, K> Collector<? super T, ?, Map<K, List<T>>> multiGroupingBy(Function<? super T, ? extends Iterable<? extends K>> classifier) {
		return multiGroupingBy(classifier, HashMap::new, Collectors.toList());
	}

	public static <T> BinaryOperator<T> mergeFail() {
		return (k0, k1) -> {
			throw new IllegalStateException("Duplicate key " + k0);
		};
	}

	public static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> multiGroupingBy(Function<? super T, ? extends Iterable<? extends K>> classifier, Supplier<M> mapFactory, Collector<? super T, A, D> downstream) {
		final Supplier<A> downstreamSupplier = downstream.supplier();
		final BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
		final BiConsumer<Map<K, A>, T> accumulator = (m, t) -> {
			final Iterable<? extends K> keys = Objects.requireNonNull(classifier.apply(t), "element cannot be mapped to a null key collection");
			for (K key : keys) {
				downstreamAccumulator.accept(m.computeIfAbsent(key, k -> downstreamSupplier.get()), t);
			}
		};
		final BinaryOperator<Map<K, A>> merger = (m1, m2) -> {
			for (Map.Entry<K, A> e : m2.entrySet()) {
				m1.merge(e.getKey(), e.getValue(), downstream.combiner());
			}
			return m1;
		};
		@SuppressWarnings("unchecked")
		final Supplier<Map<K, A>> castFactory = (Supplier<Map<K, A>>) mapFactory;

		if (downstream.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) return new SimpleCollector<>(castFactory, accumulator, merger, EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

		@SuppressWarnings("unchecked")
		final Function<A, A> downstreamFinisher = (Function<A, A>) downstream.finisher();
		final Function<Map<K, A>, M> finisher = i -> {
			i.replaceAll((k, v) -> downstreamFinisher.apply(v));
			@SuppressWarnings("unchecked")
			final M cast = (M) i;
			return cast;
		};
		return new SimpleCollector<>(castFactory, accumulator, merger, finisher, Collections.emptySet());
	}

	@RequiredArgsConstructor
	public static class SimpleCollector<T, A, R> implements Collector<T, A, R> {
		protected final Supplier<A> supplier;

		protected final BiConsumer<A, T> accumulator;

		protected final BinaryOperator<A> combiner;

		protected final Function<A, R> finisher;

		protected final Set<Characteristics> characteristics;

		public SimpleCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Set<Characteristics> characteristics) {
			this(supplier, accumulator, combiner, IFunction.cast(), characteristics);
		}

		@Override
		public BiConsumer<A, T> accumulator() {
			return accumulator;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return characteristics;
		}

		@Override
		public BinaryOperator<A> combiner() {
			return combiner;
		}

		@Override
		public Function<A, R> finisher() {
			return finisher;
		}

		@Override
		public Supplier<A> supplier() {
			return supplier;
		}
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
}
