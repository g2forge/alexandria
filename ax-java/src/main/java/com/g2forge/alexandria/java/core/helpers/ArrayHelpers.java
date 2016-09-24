package com.g2forge.alexandria.java.core.helpers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class ArrayHelpers {
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
