package com.g2forge.alexandria.java.function.tee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import com.g2forge.alexandria.java.adt.tuple.ITuple2G_;

import lombok.Getter;

/**
 * Delegate to another function, and record all of the input and output value pairs.
 * 
 * @param <I> The input type.
 * @param <O> The output type.
 */
public class RecordingFunction<I, O> extends TeeFunction<I, O> {
	/**
	 * A collection of the pairs of input and output values, for each invocation of this function.
	 * 
	 * @return The record of function applications.
	 */
	@Getter
	protected final Collection<ITuple2G_<? extends I, ? extends O>> record;

	/**
	 * Create a new recording function, delegating to the specified function.
	 * 
	 * @param function The function to delegate to. This function will determine the output values.
	 */
	public RecordingFunction(Function<I, O> function) {
		this(function, new ArrayList<>());
	}

	private RecordingFunction(Function<I, O> function, Collection<ITuple2G_<? extends I, ? extends O>> record) {
		super(function, record::add);
		this.record = record;
	}
}
