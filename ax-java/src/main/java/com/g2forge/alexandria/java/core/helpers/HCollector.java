package com.g2forge.alexandria.java.core.helpers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
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

import com.g2forge.alexandria.java.adt.tuple.ITuple2G_;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple2G_O;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
		return Collectors.collectingAndThen(Collectors.toList(), HCollection::getOne);
	}

	public static <T> Collector<T, ?, T> toFirst() {
		return Collectors.collectingAndThen(Collectors.toList(), HCollection::getFirst);
	}

	public static <T> Collector<T, ?, T> toAny() {
		return Collectors.collectingAndThen(Collectors.toList(), HCollection::getAny);
	}

	public static <T> Collector<T, ?, T[]> toArray(Class<T> type) {
		return new SimpleCollector<T, List<T>, T[]>(ArrayList::new, List::add, (left, right) -> {
			left.addAll(right);
			return left;
		}, list -> {
			@SuppressWarnings("unchecked")
			final T[] array = (T[]) Array.newInstance(type, list.size());
			return list.toArray(array);
		}, Collections.emptySet());
	}

	public static <K, V> Collector<Tuple2G_O<K, V>, ?, Map<K, V>> toMapTuples() {
		return Collectors.toMap(ITuple2G_::get0, ITuple2G_::get1, mergeFail(), () -> new LinkedHashMap<>());
	}

	public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> toMapEntries() {
		return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFail(), () -> new LinkedHashMap<>());
	}

	public static <T, K> Collector<? super T, ?, Map<K, List<T>>> multiGroupingBy(Function<? super T, ? extends Iterable<? extends K>> classifier) {
		return multiGroupingBy(classifier, LinkedHashMap::new, Collectors.toList());
	}

	public static <T> BinaryOperator<T> mergeFail() {
		return (v0, v1) -> {
			throw new IllegalStateException(String.format("Duplicate values %1$s and %2$s", v0, v1));
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
			this(supplier, accumulator, combiner, IFunction1.cast(), characteristics);
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
		}, (list0, list1) -> {
			if (!list0.isEmpty()) list0.add(separator);
			list0.addAll(list1);
			return list0;
		});
	}

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@RequiredArgsConstructor
	public class StringJoinCollector {
		/** Prefix to be used before any elements */
		@NonNull
		protected final CharSequence prefix;

		/** Normal separator, to be used between all pairs of elements, except the last two. */
		@NonNull
		protected final CharSequence separatorNormal;

		/** Last separator, to be used between the final two elements */
		@NonNull
		protected final CharSequence separatorLast;

		/** Prefix to be used after all elements */
		@NonNull
		protected final CharSequence suffix;

		/** The current set of elements, the prefix and the separators. The suffix is never added to this builder, nor is the last separator. */
		protected StringBuilder builder;

		/** The index in {@link #builder} of the most recently appended separator or {@code -1} if none has been appended. */
		protected int lastSeparatorIndex = -1;

		/**
		 * Add a new element.
		 * 
		 * @param element The element to add.
		 * @return {@code this}
		 */
		public StringJoinCollector add(CharSequence element) {
			getBuilderNext().append(element);
			return this;
		}

		public StringJoinCollector combine(StringJoinCollector that) {
			Objects.requireNonNull(that);

			final StringBuilder thatBuilder = that.getBuilder();
			if (thatBuilder != null) {
				final StringBuilder builder = getBuilderNext();
				final int thisLength = builder.length();
				final int thatPrefixLength = that.getPrefix().length();
				builder.append(thatBuilder, thatPrefixLength, thatBuilder.length());
				setLastSeparatorIndex(that.getLastSeparatorIndex() - thatPrefixLength + thisLength);
			}

			return this;
		}

		protected StringBuilder getBuilderNext() {
			final StringBuilder builder = getBuilder();
			if (builder != null) {
				setLastSeparatorIndex(builder.length());
				builder.append(getSeparatorNormal());
			} else setBuilder(new StringBuilder().append(getPrefix()));
			return getBuilder();
		}

		@Override
		public String toString() {
			final StringBuilder builder = getBuilder();
			if (builder == null) return getPrefix().toString() + getSuffix();

			final boolean hasLastSeparator = !getSeparatorNormal().equals(getSeparatorLast());
			if ((suffix.length() < 1) && !hasLastSeparator) return builder.toString();
			else {
				// Replace the last separator
				final int lastSeparatorIndex = getLastSeparatorIndex();
				if (hasLastSeparator && (lastSeparatorIndex >= 0)) builder.replace(lastSeparatorIndex, lastSeparatorIndex + getSeparatorNormal().length(), getSeparatorLast().toString());

				// Add the suffix, but remove it again right after
				final int length = builder.length();
				final String retVal = builder.append(suffix).toString();
				builder.setLength(length);

				// Switch back to the normal separator
				if (hasLastSeparator && (lastSeparatorIndex >= 0)) builder.replace(lastSeparatorIndex, lastSeparatorIndex + getSeparatorLast().length(), getSeparatorNormal().toString());

				return retVal;
			}
		}
	}

	public static Collector<CharSequence, ?, String> joining() {
		return joining("");
	}

	public static Collector<CharSequence, ?, String> joining(CharSequence separator) {
		return joining("", separator, "");
	}

	public static Collector<CharSequence, ?, String> joining(CharSequence prefix, CharSequence separator, CharSequence suffix) {
		return joining(prefix, separator, separator, suffix);
	}

	public static Collector<CharSequence, ?, String> joining(CharSequence separatorNormal, CharSequence separatorLast) {
		return joining("", separatorNormal, separatorLast, "");
	}

	public static Collector<CharSequence, ?, String> joining(CharSequence prefix, CharSequence separatorNormal, CharSequence separatorLast, CharSequence suffix) {
		return new SimpleCollector<>(() -> new StringJoinCollector(prefix, separatorNormal, separatorLast, suffix), StringJoinCollector::add, StringJoinCollector::combine, StringJoinCollector::toString, Collections.emptySet());
	}
}
