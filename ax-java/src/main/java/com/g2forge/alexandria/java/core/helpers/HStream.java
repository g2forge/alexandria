package com.g2forge.alexandria.java.core.helpers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.g2forge.alexandria.java.marker.Helpers;
import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.implementations.Tuple2G_O;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HStream {
	@SafeVarargs
	public static <T> Stream<T> concat(Stream<? extends T>... streams) {
		return Arrays.stream(streams).reduce(Stream::concat).get().map(t -> t);
	}

	public static <T> T findOne(Stream<? extends T> stream) {
		final List<T> list = stream.collect(Collectors.toList());
		if (list.size() != 1) throw new IllegalArgumentException();
		return list.get(0);
	}

	public static <I, O> O iterate(Stream<? extends I> stream, O initial, BiFunction<? super I, ? super O, ? extends O> mutate) {
		final Object[] value = new Object[] { initial };
		stream.forEachOrdered(input -> {
			@SuppressWarnings("unchecked")
			final O current = (O) value[0];
			value[0] = mutate.apply(input, current);
		});

		@SuppressWarnings("unchecked")
		final O current = (O) value[0];
		return current;
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

	public static <T> Stream<T> toStream(Iterator<T> iterator) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
	}

	public static <T> Stream<T> toStream(Supplier<T> supplier) {
		return Stream.of(supplier).map(Supplier::get);
	}

	public static <T> Stream<? extends ITuple2G_<Integer, T>> toStreamIndexed(List<? extends T> list) {
		return IntStream.range(0, list.size()).mapToObj(i -> new Tuple2G_O<>(i, list.get(i)));
	}

	public static <T> Stream<T> subtype(Stream<?> stream, Class<T> subtype) {
		return stream.filter(s -> subtype.isInstance(s)).map(subtype::cast);
	}

	public static <I0, I1, O> Stream<O> zip(Stream<? extends I0> stream0, Stream<? extends I1> stream1, BiFunction<? super I0, ? super I1, ? extends O> func) {
		Objects.requireNonNull(func);
		final Spliterator<? extends I0> spliterator0 = Objects.requireNonNull(stream0).spliterator();
		final Spliterator<? extends I1> spliterator1 = Objects.requireNonNull(stream1).spliterator();

		final Iterator<O> iteratorO = new Iterator<O>() {
			protected final Iterator<I0> iterator0 = Spliterators.iterator(spliterator0);

			protected final Iterator<I1> iterator1 = Spliterators.iterator(spliterator1);

			@Override
			public boolean hasNext() {
				return iterator0.hasNext() && iterator1.hasNext();
			}

			@Override
			public O next() {
				return func.apply(iterator0.next(), iterator1.next());
			}
		};

		// Characteristics intersection, and the result isn't sorted
		final int characteristics = spliterator0.characteristics() & spliterator1.characteristics() & ~Spliterator.SORTED;
		// Compute the size if we can
		final long size = ((characteristics & Spliterator.SIZED) != 0) ? Math.min(spliterator0.getExactSizeIfKnown(), spliterator1.getExactSizeIfKnown()) : -1;
		final Spliterator<O> split = Spliterators.spliterator(iteratorO, size, characteristics);
		return (stream0.isParallel() || stream1.isParallel()) ? StreamSupport.stream(split, true) : StreamSupport.stream(split, false);
	}
}
