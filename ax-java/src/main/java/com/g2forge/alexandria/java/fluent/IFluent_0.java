package com.g2forge.alexandria.java.fluent;

import java.util.function.Supplier;

/**
 * "0" indicates that the arity of this fluent holder can be 0.
 */
public interface IFluent_0<T> extends IFluent__<T> {
	public T or(T other);

	public T orGet(Supplier<? extends T> other);

	public <X extends Throwable> T orThrow(Supplier<? extends X> exception) throws X;
}
