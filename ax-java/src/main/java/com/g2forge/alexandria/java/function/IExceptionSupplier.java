package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IExceptionSupplier<T, E extends Throwable> {
	public T get() throws E;
}