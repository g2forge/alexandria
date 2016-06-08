package com.g2forge.alexandria.generic.java.map;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MapHelpers {
	@SafeVarargs
	public static <A, B> B[] map(final Class<? super B> bType, final Function<? super A, ? extends B> map, final A... values) {
		@SuppressWarnings("unchecked")
		final B[] retVal = (B[]) Array.newInstance(bType, values.length);
		for (int i = 0; i < values.length; i++) {
			retVal[i] = map.apply(values[i]);
		}
		return retVal;
	}

	@SafeVarargs
	public static <A, B> List<B> map(final Function<? super A, ? extends B> map, final A... values) {
		final List<B> retVal = new ArrayList<>();
		for (int i = 0; i < values.length; i++) {
			retVal.add(map.apply(values[i]));
		}
		return retVal;
	}
}
