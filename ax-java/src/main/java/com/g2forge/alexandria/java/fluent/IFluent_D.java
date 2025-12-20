package com.g2forge.alexandria.java.fluent;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The "D" in the class name indicates that the arity can go down - in other words operations on the fluent holder can result in a fluent holder with fewer
 * values than this one.
 */
public interface IFluent_D<T> extends IFluent__<T> {
	/**
	 * Filter the values in this holder to create a new holder with only the values the predicate accepts.
	 * 
	 * @param predicate A predicate to use to test the values in this holder.
	 * @return A holder with only the values accepted by the predicate.
	 */
	public IFluent__<T> filter(Predicate<? super T> predicate);

	/**
	 * Apply the specified function to all the values in this holder, and collect all the non-empty return values into a new holder.
	 * 
	 * @param <U> The type of the values in the returned holders.
	 * @param mapper A function which accepts values from this holder, and returns fluents with at most one value.
	 * @return A holder with the values, if any, from the holders returned by the mapper.
	 */
	public <U> IFluent_D<U> flatMap(Function<? super T, ? extends IFluent1_<U>> mapper);
}
