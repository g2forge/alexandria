package com.g2forge.alexandria.java.concurrent;

/**
 * A complete asynchronous message pair, consisting of a future and a promise which are linked. Generally this interface will only be used as the return type
 * from a factory, as no consumer of a asynchronous message interface should generally need both the future and the promise.
 * 
 * @author gdgib
 *
 * @param <T>
 */
public interface IAsynchronousMessage<T> {
	public IFuture<T> getFuture();

	public IPromise<T> getPromise();
}
