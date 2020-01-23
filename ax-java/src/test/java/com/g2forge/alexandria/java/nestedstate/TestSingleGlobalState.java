package com.g2forge.alexandria.java.nestedstate;

public class TestSingleGlobalState extends ATestNestedState {
	@Override
	public <T> INestedState<T> create() {
		return new SingleGlobalState<>();
	}
}
