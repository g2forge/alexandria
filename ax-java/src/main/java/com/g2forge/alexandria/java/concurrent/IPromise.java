package com.g2forge.alexandria.java.concurrent;

import com.g2forge.alexandria.java.adt.tuple.ITuple1_S;
import com.g2forge.alexandria.java.function.IRunnable;

public interface IPromise<T> extends ITuple1_S<T>, IRunnable, IOpaquePromise<T> {
	/**
	 * Make the current value of this promise the final value, and notify any futures.
	 */
	@Override
	public void run();

	@Override
	public IPromise<T> set0(T value);
}
