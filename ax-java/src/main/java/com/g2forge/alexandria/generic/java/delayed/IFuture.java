package com.g2forge.alexandria.generic.java.delayed;

import com.g2forge.alexandria.generic.java.tuple.ITuple1G_;

public interface IFuture<T> extends ITuple1G_<T> {
	/**
	 * Synchronously get the value of this future, potentially blocking until it is resolved.
	 */
	@Override
	public T get0();
}
