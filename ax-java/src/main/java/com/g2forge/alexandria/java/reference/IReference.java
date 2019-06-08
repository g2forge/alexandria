package com.g2forge.alexandria.java.reference;

import com.g2forge.alexandria.java.function.ISupplier;

public interface IReference<T> extends ISupplier<T> {
	public void clear();
}
