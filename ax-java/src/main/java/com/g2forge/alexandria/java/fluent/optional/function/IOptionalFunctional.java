package com.g2forge.alexandria.java.fluent.optional.function;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;

public interface IOptionalFunctional<I, O, F extends IOptionalFunctional<? super I, ? extends O, ?>> {
	/**
	 * Create a function which will fall back to the return values from <code>fallback</code> when this function doesn't return a value.
	 * 
	 * @param fallback
	 * @return
	 */
	public Function<I, O> fallback(Function<? super I, ? extends O> fallback);

	/**
	 * Create a function where any value returned from <code>override</code> overrides the value this function would return.
	 * 
	 * @param override
	 * @return
	 */
	public IOptionalFunctional<I, O, F> override(F override);

	public IOptionalFunctional<I, O, F> recursive(BiPredicate<? super I, ? super I> terminate, boolean prior, Class<I> type);

	/**
	 * Create a function which returns an empty value for all of those not accepted by the specified predicate.
	 * 
	 * @param predicate
	 * @param factory
	 * @return
	 */
	public IOptionalFunctional<I, O, F> restrict(Predicate<? super I> predicate, IOptionalFactory factory);
}
