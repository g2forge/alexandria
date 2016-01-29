package com.g2forge.alexandria.java.concurrent.implementations;

import java.util.concurrent.CountDownLatch;

import com.g2forge.alexandria.java.concurrent.IAsynchronousMessage;
import com.g2forge.alexandria.java.concurrent.IFuture;
import com.g2forge.alexandria.java.concurrent.IPromise;

public class AsynchronousMessage<T> implements IAsynchronousMessage<T> {
	protected final IFuture<T> future = new IFuture<T>() {
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
	};

	protected final IPromise<T> promise = new IPromise<T>() {
		@Override
		public void invoke() {
			latch.countDown();
		}

		@Override
		public IPromise<T> set0(final T value) {
			AsynchronousMessage.this.value = value;
			return this;
		}
	};

	protected final CountDownLatch latch = new CountDownLatch(1);

	protected T value;

	public AsynchronousMessage() {}

	@Override
	public IFuture<T> getFuture() {
		return future;
	}

	@Override
	public IPromise<T> getPromise() {
		return promise;
	}

}
