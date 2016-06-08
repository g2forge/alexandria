package com.g2forge.alexandria.java.concurrent;

import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Constant<T> implements IFuture<T>, Supplier<T> {
	protected final T value;

	@Override
	public T get() {
		return value;
	}

	@Override
	public T get0() {
		return value;
	}
}
