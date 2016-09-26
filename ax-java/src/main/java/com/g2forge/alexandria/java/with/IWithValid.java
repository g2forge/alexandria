package com.g2forge.alexandria.java.with;

import com.g2forge.alexandria.java.function.ISupplier;

public interface IWithValid<T> extends ISupplier<T> {
	public boolean isValid();
}
