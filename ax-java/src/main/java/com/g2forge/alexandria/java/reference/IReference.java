package com.g2forge.alexandria.java.reference;

import java.util.function.Supplier;

public interface IReference<T> extends Supplier<T> {
	public void clear();
}
