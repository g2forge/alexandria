package com.g2forge.alexandria.java;

@FunctionalInterface
public interface IFactory<T> {
	public T create();
}
