package com.g2forge.alexandria.generic.java.delayed.implementations;

import java.util.concurrent.CountDownLatch;

import com.g2forge.alexandria.generic.java.delayed.IFuture;
import com.g2forge.alexandria.generic.java.delayed.IPromise;

public class Promise<T> implements IPromise<T> {
	protected class Future implements IFuture<T> {
		@Override
		public T get0() {
			try {
				latch.await();
			} catch (final InterruptedException exception) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(exception);
			}
			return value;
		}
	}
	
	protected final CountDownLatch latch = new CountDownLatch(1);
	
	protected final Future future = new Future();
	
	protected T value;
	
	public IFuture<T> getFuture() {
		return future;
	}
	
	@Override
	public void invoke() {
		latch.countDown();
	}
	
	@Override
	public IPromise<T> set0(final T value) {
		this.value = value;
		return this;
	}
}
