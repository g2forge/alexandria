package com.g2forge.alexandria.java.concurrent;

/**
 * A complete asynchronous message slot, consisting of a future and a promise which are linked. Generally this interface will only be used as the return type
 * from a factory, as no consumer of slot should generally need both the future and the promise.
 * 
 * @author gdgib
 *
 * @param <T>
 */
public interface ISlot__<T> {
	public IOpaqueFuture<T> getFuture();

	public IOpaquePromise<T> getPromise();
}
