package com.g2forge.alexandria.java.function;

import java.util.function.Function;

public class FunctionHelpers {
	public static <I, X, O> Function<? super I, ? extends O> compose(Function<? super I, ? extends X> f0, Function<? super X, ? extends O> f1) {
		return f0.andThen(f1);
	}

	public static <I, X0, X1, O> Function<? super I, ? extends O> compose(Function<? super I, ? extends X0> f0, Function<? super X0, ? extends X1> f1, Function<? super X1, ? extends O> f2) {
		return f0.andThen(f1).andThen(f2);
	}
}
