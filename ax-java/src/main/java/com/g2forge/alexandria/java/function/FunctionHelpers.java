package com.g2forge.alexandria.java.function;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FunctionHelpers {
	public static <I, X, O> Function<? super I, ? extends O> compose(Function<? super I, ? extends X> f0, Function<? super X, ? extends O> f1) {
		return f0.andThen(f1);
	}

	public static <I, X0, X1, O> Function<? super I, ? extends O> compose(Function<? super I, ? extends X0> f0, Function<? super X0, ? extends X1> f1, Function<? super X1, ? extends O> f2) {
		return f0.andThen(f1).andThen(f2);
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
