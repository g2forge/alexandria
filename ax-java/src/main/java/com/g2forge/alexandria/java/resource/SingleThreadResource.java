package com.g2forge.alexandria.java.resource;

import com.g2forge.alexandria.java.close.ICloseable;

public class SingleThreadResource<T> implements IThreadResource<T> {
	protected final ThreadLocal<T> local = new ThreadLocal<>();

	public void close(T value) {
		if (local.get() != value) throw new IllegalStateException();
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
