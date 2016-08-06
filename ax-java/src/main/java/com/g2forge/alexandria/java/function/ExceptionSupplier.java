package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface ExceptionSupplier<T, E extends Throwable> {
	public T get() throws E;
}