package com.g2forge.alexandria.java.nestedstate;

import com.g2forge.alexandria.java.close.ICloseable;

public class FlagState implements INestedState<Boolean> {
	protected boolean inside;

	protected void close(boolean expected, boolean previous) {
		if (inside != expected) throw new IllegalStateException();
		this.inside = previous;
	}

	@Override
	public Boolean get() {
		return inside;
	}

	@Override
	public ICloseable open(Boolean value) {
		final boolean previous = inside;
		final boolean expected = this.inside |= value;
		return () -> close(expected, previous);
	}
}
