package com.g2forge.alexandria.java.concurrent;

public interface ISlot<T> extends IOpaqueSlot<T> {
	public IFuture<T> getFuture();

	public IPromise<T> getPromise();
}
