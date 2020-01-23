package com.g2forge.alexandria.java.nestedstate;

public class TestStackGlobalState extends ATestNestedState {
	@Override
	public <T> INestedState<T> create() {
		return new StackGlobalState<>();
	}
}
