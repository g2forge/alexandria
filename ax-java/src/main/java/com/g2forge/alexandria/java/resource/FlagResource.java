package com.g2forge.alexandria.java.resource;

import com.g2forge.alexandria.java.close.ICloseable;

public class FlagResource implements IThreadResource<Boolean> {
	protected boolean inside;

	public void close(boolean previous) {
		this.inside = previous;
	}

	@Override
	public Boolean get() {
		return inside;
	}

	@Override
	public ICloseable open(Boolean value) {
		final boolean previous = inside;
		this.inside |= value;
		return () -> close(previous);
	}
}
