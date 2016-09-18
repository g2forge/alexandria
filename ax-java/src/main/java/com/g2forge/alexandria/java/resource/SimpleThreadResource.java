package com.g2forge.alexandria.java.resource;

import com.g2forge.alexandria.java.close.ICloseable;

public class SimpleThreadResource<T> implements IThreadResource<T> {
	protected boolean valid;

	protected T value;

	public void close(T value) {
		if (!valid || (this.value != value)) throw new IllegalStateException();
		this.value = null;
		this.valid = false;
	}

	@Override
	public T get() {
		return this.value;
	}

	@Override
	public ICloseable open(T value) {
		if (valid || (value != null)) throw new IllegalStateException();
		this.value = value;
		this.valid = true;
		return () -> close(value);
	}
}
