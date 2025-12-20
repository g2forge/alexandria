package com.g2forge.alexandria.java.fluent;

import com.g2forge.alexandria.java.function.ISupplier;

/**
 * The "1" in the class name indicates that the arity of this fluent holder is at most 1 - in other words it cannot have 2 or more values.
 */
public interface IFluent1_<T> extends IFluentG_<T>, ISupplier<T> {
	@Override
	public T get();
}
