package com.g2forge.alexandria.java.function.tee;

import java.util.function.Consumer;
import java.util.function.Function;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.implementations.Tuple2GSO;

import lombok.AllArgsConstructor;

/**
 * Delegate to another function, and for each invocation inform a consumer about both the input and output. Before mapping a {@link java.util.stream.Stream}
 * over this function, consider using {@link java.util.stream.Stream#peek(Consumer)}.
 * 
 * @param <I> The input type.
 * @param <O> The output type.
 */
@AllArgsConstructor
public class TeeFunction<I, O> implements IFunction1<I, O> {
	protected final Function<I, O> function;

	protected final Consumer<? super ITuple2G_<? extends I, ? extends O>> consumer;

	@Override
	public O apply(I input) {
		final O retVal = function.apply(input);
		consumer.accept(new Tuple2GSO<>(input, retVal));
		return retVal;
	}
}
