package com.g2forge.alexandria.java.fluent;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * The "G" in the class name indicates that the values in this fluent holder can be gotten - in other words the caller can find out what's in this holder.
 * 
 * @param <T>
 */
public interface IFluentG_<T> extends IFluent__<T> {
	/**
	 * Test if this fluent holder is empty.
	 * 
	 * @return <code>true</code> if this fluent holder contains no values.
	 */
	public boolean isEmpty();

	/**
	 * Test if this fluent holder is not empty.
	 * 
	 * @return <code>true</code> if this fluent holder contains one or more values.
	 */
	public default boolean isNotEmpty() {
		return !isEmpty();
	}

	/**
	 * Convert the values in this fluent holder to a stream.
	 * 
	 * @return A stream containing the values held.
	 */
	public Stream<T> toStream();

	/**
	 * Visit the values in this fluent holder.
	 * 
	 * @param consumer This method will call <code>consumer.accept(...)</code> for each value in this fluent holder.
	 */
	public void visit(Consumer<? super T> consumer);
}
