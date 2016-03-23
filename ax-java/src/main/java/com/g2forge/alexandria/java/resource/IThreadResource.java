package com.g2forge.alexandria.java.resource;

import com.g2forge.alexandria.java.close.ICloseable;

public interface IThreadResource<T> {
	public T get();

	public ICloseable open(T value);
}
