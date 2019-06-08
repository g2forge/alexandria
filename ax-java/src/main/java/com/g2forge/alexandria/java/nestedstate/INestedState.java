package com.g2forge.alexandria.java.nestedstate;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.ISupplier;

public interface INestedState<T> extends ISupplier<T> {
	public ICloseable open(T value);
}
