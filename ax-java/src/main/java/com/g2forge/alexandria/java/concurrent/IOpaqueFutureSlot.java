package com.g2forge.alexandria.java.concurrent;

public interface IOpaqueFutureSlot<T> extends IOpaqueSlot<T> {
	public IPromise<T> getPromise();
}
