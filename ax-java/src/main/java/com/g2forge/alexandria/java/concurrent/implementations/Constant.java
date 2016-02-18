package com.g2forge.alexandria.java.concurrent.implementations;

import com.g2forge.alexandria.java.concurrent.IFuture;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Constant<T> implements IFuture<T> {
	protected final T value;

	@Override
	public T get0() {
		return value;
	}
}
