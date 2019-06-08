package com.g2forge.alexandria.java.concurrent;

import java.util.concurrent.CountDownLatch;

public class Slot<T> implements ISlotPF<T> {
	protected final IFuture<T> future = new IFuture<T>() {
		@Override
		public T get0() {
			try {
				latch.await();
			} catch (final InterruptedException exception) {
				Thread.currentThread().interrupt();
				throw new RuntimeInterruptedException(exception);
			}
			return value;
		}
	};

	protected final IPromise<T> promise = new IPromise<T>() {
		@Override
		public void run() {
			latch.countDown();
		}

		@Override
		public IPromise<T> set0(final T value) {
			Slot.this.value = value;
			return this;
		}
	};

	protected final CountDownLatch latch = new CountDownLatch(1);

	protected T value;

	public Slot() {}

	@Override
	public IFuture<T> getFuture() {
		return future;
	}

	@Override
	public IPromise<T> getPromise() {
		return promise;
	}
}
