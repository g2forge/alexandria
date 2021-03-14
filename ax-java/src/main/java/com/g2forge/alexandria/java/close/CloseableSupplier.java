package com.g2forge.alexandria.java.close;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.io.HIO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CloseableSupplier<T> implements ICloseableSupplier<T> {
	protected final IFunction1<? super IConsumer1<? super AutoCloseable>, ? extends T> compute;

	protected List<AutoCloseable> closeables = null;

	protected T value = null;

	@Override
	public void close() {
		if (closeables == null) return;
		HIO.closeAll(closeables);
		closeables = null;
	}

	@Override
	public T get() {
		if (closeables == null) {
			closeables = new ArrayList<>();
			try {
				final IConsumer1<? super AutoCloseable> consumer = closeable -> {
					if (closeable != null) closeables.add(closeable);
				};
				value = compute.apply(consumer);
			} catch (Throwable throwable) {
				close();
				throw throwable;
			}
		}
		return value;
	}
}
