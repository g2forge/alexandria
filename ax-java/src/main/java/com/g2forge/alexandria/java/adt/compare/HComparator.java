package com.g2forge.alexandria.java.adt.compare;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@UtilityClass
@Helpers
public class HComparator {
	@SafeVarargs
	public static <T> Comparator<T> createRecordComparator(Function<? super T, ? extends Comparable<?>>... accessors) {
		final List<Comparator<T>> comparators = HCollection.asList(accessors).stream().map(a -> {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final Comparator<T> retVal = new MappedComparator(a, ComparableComparator.create());
			return retVal;
		}).collect(Collectors.toList());
		return new ChainedComparator<>(comparators);
	}
}
