package com.g2forge.alexandria.java.function;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class AScopedSupplier<T> implements IScopedSupplier<T> {
	protected enum State {
		New,
		Initialized,
		Clean;
	}

	protected State state = State.New;

	@Getter(value = AccessLevel.PROTECTED, lazy = true)
	private final T value = computeAndMark();

	protected abstract void cleanup();

	@Override
	public void close() {
		switch (state) {
			case New:
			case Clean:
				return;
			case Initialized:
				try {
					cleanup();
				} finally {
					state = State.Clean;
				}
		}
	}

	protected abstract T compute();

	private T computeAndMark() {
		state = State.Initialized;
		return compute();
	}

	@Override
	public T get() {
		if (State.Clean.equals(state)) throw new IllegalStateException();
		return getValue();
	}

}
