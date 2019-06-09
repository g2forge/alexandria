package com.g2forge.alexandria.java.concurrent;

import com.g2forge.alexandria.java.adt.tuple.ITuple1G_;

public interface IFuture<T> extends ITuple1G_<T>, IOpaqueFuture<T> {
	/**
	 * Synchronously get the value of this future, potentially blocking until it is resolved.
	 */
	@Override
	public T get0();
}
