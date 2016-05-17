package com.g2forge.alexandria.java.concurrent.implementations;

import java.util.concurrent.CountDownLatch;

import com.g2forge.alexandria.java.concurrent.IFuture;
import com.g2forge.alexandria.java.concurrent.IPromise;
import com.g2forge.alexandria.java.concurrent.ISlot;
import com.g2forge.alexandria.java.concurrent.RuntimeInterruptedException;

public class Slot<T> implements ISlot<T> {
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
		public void invoke() {
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
