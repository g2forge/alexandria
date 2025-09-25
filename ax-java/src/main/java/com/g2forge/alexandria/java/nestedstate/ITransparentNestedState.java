package com.g2forge.alexandria.java.nestedstate;

import java.util.List;

public interface ITransparentNestedState<T> extends INestedState<T> {
	public int depth();

	public List<T> getCurrent();
}
