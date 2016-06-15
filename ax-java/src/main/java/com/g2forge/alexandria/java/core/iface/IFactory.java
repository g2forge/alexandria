package com.g2forge.alexandria.java.core.iface;

@FunctionalInterface
public interface IFactory<T> {
	public T create();
}
