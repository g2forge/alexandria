package com.g2forge.alexandria.java.resource;

import java.util.function.Supplier;

import com.g2forge.alexandria.java.close.ICloseable;

public interface ICloseableResource<T> extends Supplier<T> {
	public ICloseable open(T value);
}
