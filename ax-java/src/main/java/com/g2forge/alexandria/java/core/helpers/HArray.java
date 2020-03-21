package com.g2forge.alexandria.java.core.helpers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HArray {
	@SafeVarargs
	public static <T> T[] create(T... array) {
		return array;
	}

	@SuppressWarnings("unchecked")
	public static <I extends O, O> O[] cast(Class<? super O> type, I... array) {
		final Object retVal = Array.newInstance(type, array.length);
		for (int i = 0; i < array.length; i++) {
			Array.set(retVal, i, type.cast(array[i]));
		}
		return (O[]) retVal;
	}

	@SafeVarargs
	public static <T> T[] concatenate(T[]... arrays) {
		if (arrays.length == 1) return arrays[0];
		@SuppressWarnings("unchecked")
		final T[] retVal = (T[]) Array.newInstance(arrays.getClass().getComponentType().getComponentType(), Stream.of(arrays).mapToInt(a -> a.length).sum());
		int base = 0;
		for (T[] array : arrays) {
			System.arraycopy(array, 0, retVal, base, array.length);
			base += array.length;
		}
		return retVal;
	}

	@SafeVarargs
	public static <T> boolean contains(T value, T... array) {
		for (int i = 0; i < array.length; i++)
			if (Objects.equals(value, array[i])) return true;
		return false;
	}

	@SafeVarargs
	public static <A, B> B[] map(final Class<? super B> type, final Function<? super A, ? extends B> function, final A... values) {
		@SuppressWarnings("unchecked")
		final B[] retVal = (B[]) Array.newInstance(type, values.length);
		for (int i = 0; i < values.length; i++)
			retVal[i] = function.apply(values[i]);
		return retVal;
	}

	@SafeVarargs
	public static <A, B> List<B> map(final Function<? super A, ? extends B> function, final A... values) {
		final List<B> retVal = new ArrayList<>();
		for (int i = 0; i < values.length; i++)
			retVal.add(function.apply(values[i]));
		return retVal;
	}
}
