package com.g2forge.alexandria.generic.java.delayed;

import com.g2forge.alexandria.java.ICallback;
import com.g2forge.alexandria.java.tuple.ITuple1_S;

public interface IPromise<T> extends ITuple1_S<T>, ICallback {
	/**
	 * Make the current value of this promise the final value, and notify any futures.
	 */
	@Override
	public void invoke();

	@Override
	public IPromise<T> set0(T value);
}
