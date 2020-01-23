package com.g2forge.alexandria.java.nestedstate;

import java.util.EmptyStackException;

public class TestStackGlobalState extends ATestNestedState {
	@Override
	protected <T> void assertEmpty(INestedState<T> state) {
		assertException(EmptyStackException.class, null, () -> state.get());
		assertException(EmptyStackException.class, null, () -> ((ICloseableNestedState<T>) state).close(null));
	}

	@Override
	protected <T> INestedState<T> create() {
		return new StackGlobalState<>();
	}
}
