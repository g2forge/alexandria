package com.g2forge.alexandria.java.fluent;

import java.util.function.Function;

/**
 * The "1" and "0" in the class name indicates that the arity of this fluent holder is either 0 or 1 - it may hold no value, or a single value.
 */
public interface IFluent10<T> extends IFluent1_<T>, IFluent_0<T> {
	/**
	 * Create a fluent holder with the value of this one, if any, and the value of the specified fallback if this holder is empty. This is the opposite of
	 * {@code #override(IFluent10)}.
	 * 
	 * @param fallback The fallback holder to use if this holder is empty.
	 * @return The created fluent holder.
	 */
	public IFluent10<T> fallback(IFluent10<? extends T> fallback);

	@Override
	public <U> IFluent10<U> map(Function<? super T, ? extends U> mapper);

	/**
	 * Create a fluent holder with the value of the specified holder, if any, and the value of this one, if the specified holder is empty. This is the opposite
	 * of {@link #fallback(IFluent10)}.
	 * 
	 * @param override The fallback holder to use if it has a value, fallback to this holders value if the specified one is empty.
	 * @return The created fluent holder.
	 */
	public IFluent10<T> override(IFluent10<? extends T> override);
}
