package com.g2forge.alexandria.java.nestedstate;

import com.g2forge.alexandria.java.close.ICloseable;

public class SingleGlobalState<T> implements ICloseableNestedState<T> {
	protected boolean valid;

	protected T value;

	public void close(T expected) {
		if (!valid || (this.value != expected)) throw new IllegalStateException();
		this.value = null;
		this.valid = false;
	}

	@Override
	public T get() {
		return this.value;
	}

	@Override
	public ICloseable open(T value) {
		if (valid || (this.value != null)) throw new IllegalStateException();
		this.value = value;
		this.valid = true;
		return () -> close(value);
	}
}
