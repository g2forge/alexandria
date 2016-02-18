package com.g2forge.alexandria.java.concurrent;

public interface ISlot<T> extends IOpaqueFutureSlot<T> {
	public IFuture<T> getFuture();
}
