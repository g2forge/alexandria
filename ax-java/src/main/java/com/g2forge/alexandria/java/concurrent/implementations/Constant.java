package com.g2forge.alexandria.java.concurrent.implementations;

import java.util.function.Supplier;

import com.g2forge.alexandria.java.concurrent.IFuture;

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
