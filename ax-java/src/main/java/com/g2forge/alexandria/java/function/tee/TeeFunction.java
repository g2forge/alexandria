package com.g2forge.alexandria.java.function.tee;

import java.util.function.Consumer;
import java.util.function.Function;

import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.Tuple2GS;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TeeFunction<I, O> implements Function<I, O> {
	protected final Function<I, O> function;

	protected final Consumer<? super ITuple2G_<? extends I, ? extends O>> consumer;

	@Override
	public O apply(I input) {
		final O retVal = function.apply(input);
		consumer.accept(new Tuple2GS<>(input, retVal));
		return retVal;
	}
}
