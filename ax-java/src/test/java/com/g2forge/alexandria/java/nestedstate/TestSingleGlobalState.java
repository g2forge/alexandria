package com.g2forge.alexandria.java.nestedstate;

import org.junit.Assert;

public class TestSingleGlobalState extends ATestNestedState {
	@Override
	protected <T> void assertEmpty(INestedState<T> state) {
		Assert.assertNull(state.get());
		assertException(IllegalStateException.class, null, () -> ((ICloseableNestedState<T>) state).close(null));
	}

	@Override
	protected <T> INestedState<T> create() {
		return new SingleGlobalState<>();
	}
}
