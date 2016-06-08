package com.g2forge.alexandria.java.iterator;

public abstract class AIterator<T> implements IIterator<T> {
	protected void check() {
		if (!isValid()) { throw new IllegalStateException("Iterator is not in a valid state!"); }
	}
}
