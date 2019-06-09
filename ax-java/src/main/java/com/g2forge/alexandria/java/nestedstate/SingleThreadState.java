package com.g2forge.alexandria.java.nestedstate;

import com.g2forge.alexandria.java.close.ICloseable;

public class SingleThreadState<T> implements ICloseableNestedState<T> {
	protected final ThreadLocal<T> local = new ThreadLocal<>();

	public void close(T expected) {
		if (local.get() != expected) throw new IllegalStateException();
		local.remove();
	}

	@Override
	public T get() {
		return local.get();
	}

	@Override
	public ICloseable open(T value) {
		if (local.get() != null) throw new IllegalStateException();
		local.set(value);
		return () -> close(value);
	}
}
