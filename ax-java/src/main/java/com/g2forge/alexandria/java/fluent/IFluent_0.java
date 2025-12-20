package com.g2forge.alexandria.java.fluent;

import java.util.function.Supplier;

/**
 * The "0" in the class name indicates that the arity of this fluent holder can be 0 - in other words it can be empty.
 */
public interface IFluent_0<T> extends IFluent__<T> {
	/**
	 * Get the value held, or the specified other value if this holder is empty.
	 * 
	 * @param other The value to return if this holder is empty.
	 * @return The value held, or the specified other value if this holder is empty.
	 */
	public T or(T other);

	/**
	 * Get the value held, or the value from the supplier if this holder is empty.
	 * 
	 * @param other A supplier to use to generate the return value if this holder is empty.
	 * @return The value held, or the value from the supplier if this holder is empty.
	 */
	public T orGet(Supplier<? extends T> other);

	/**
	 * Get the value held, or throw an exception.
	 * 
	 * @param <X> The type of the exception to throw if this holder is empty.
	 * @param exception
	 * @return
	 * @throws X
	 */
	public <X extends Throwable> T orThrow(Supplier<? extends X> exception) throws X;
}
