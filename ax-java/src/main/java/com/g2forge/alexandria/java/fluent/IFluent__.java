package com.g2forge.alexandria.java.fluent;

import java.util.function.Function;

/**
 * A holder for values which can be manipulated in the fluent programming style. Flag 0 indicates whether elements may be accessed, and how. Flag 1 indicates
 * what changes in arity are allowed.
 */
public interface IFluent__<T> {
	/**
	 * Apply the specified function to all the values in this holder, and return a new holder with the same number of values.
	 * 
	 * @param <U> The type of the resulting values.
	 * @param mapper A mapper function to convert values.
	 * @return A fluent holder with the values returned by the mapper.
	 */
	public <U> IFluent__<U> map(Function<? super T, ? extends U> mapper);
}
