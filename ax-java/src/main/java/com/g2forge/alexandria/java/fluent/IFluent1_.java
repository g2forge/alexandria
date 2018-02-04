package com.g2forge.alexandria.java.fluent;

import com.g2forge.alexandria.java.function.ISupplier;

public interface IFluent1_<T> extends IFluentG_<T>, ISupplier<T> {
	public T get();
}
