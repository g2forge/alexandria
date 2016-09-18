package com.g2forge.alexandria.java.resource;

import com.g2forge.alexandria.java.close.ICloseable;

public class CountResource implements ICloseableResource<Integer> {
	protected int nesting;

	public void close(int previous) {
		this.nesting = previous;
	}

	@Override
	public Integer get() {
		return nesting;
	}

	@Override
	public ICloseable open(Integer value) {
		final int previous = nesting;
		this.nesting += value;
		return () -> close(previous);
	}
}
