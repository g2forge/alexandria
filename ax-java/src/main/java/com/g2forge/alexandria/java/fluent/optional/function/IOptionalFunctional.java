package com.g2forge.alexandria.java.fluent.optional.function;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;

public interface IOptionalFunctional<I, O, F extends IOptionalFunctional<? super I, ? extends O, ?>> {
	/**
	 * Create a function which will fall back to the return values from <code>fallback</code> when this function doesn't return a value.
	 * 
	 * @param fallback The function to call back to when <code>this</code> returns no value.
	 * @return A function which will return values from <code>this</code> or, if needed, the fallback.
	 * @see #override(IOptionalFunctional)
	 */
	public Function<I, O> fallback(Function<? super I, ? extends O> fallback);

	/**
	 * Create a function where any value returned from <code>override</code> overrides the value this function would return.
	 * 
	 * @param override The function whose return values, when any, override our own.
	 * @return An optional function which returns the results of <code>override</code> if any, and the result of this optional function otherwise.
	 * @see IOptionalFunctional#fallback(Function)
	 */
	public IOptionalFunctional<I, O, F> override(F override);

	public IOptionalFunctional<I, O, F> recursive(BiPredicate<? super I, ? super I> terminate, boolean prior, Class<I> type);

	/**
	 * Create a function which returns an empty value for all of those not accepted by the specified predicate.
	 * 
	 * @param predicate When the predicate accepts an input, return the value from <code>this</code> (whether empty or not).
	 * @param factory An optional factory, necessary to construct the necessary empty values when the predicate does not accept an input.
	 * @return An optional functional which returns the same values as <code>this</code> when the predicate accepts an input, and
	 *         {@link IOptionalFactory#empty()} otherwise.
	 */
	public IOptionalFunctional<I, O, F> restrict(Predicate<? super I> predicate, IOptionalFactory factory);
}
