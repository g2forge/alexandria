package com.g2forge.alexandria.java.nestedstate;

import com.g2forge.alexandria.java.close.ICloseable;

public class CountState implements INestedState<Integer> {
	protected int nesting;

	protected void close(int expected, int previous) {
		if (nesting != expected) throw new IllegalStateException();
		this.nesting = previous;
	}

	@Override
	public Integer get() {
		return nesting;
	}

	@Override
	public ICloseable open(Integer value) {
		final int previous = nesting;
		final int expected = this.nesting += value;
		return () -> close(expected, previous);
	}
}
