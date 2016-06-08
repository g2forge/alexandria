package com.g2forge.alexandria.generic.java.iterator.implementations;

import com.g2forge.alexandria.generic.java.iterator.AIterator;
import com.g2forge.alexandria.generic.java.iterator.IIterator;

public class ArrayIterator<T> extends AIterator<T> {
	protected final ArrayIterator<T> parent;
	
	protected final T[] array;
	
	protected int i;
	
	protected ArrayIterator(ArrayIterator<T> parent) {
		this.parent = parent;
		this.array = parent.array;
		this.i = parent.i;
	}
	
	@SafeVarargs
	public ArrayIterator(T... array) {
		this.parent = null;
		this.array = array;
		this.i = 0;
	}
	
	@Override
	public void commit() {
		if (parent != null) {
			parent.i = i;
		}
	}
	
	@Override
	public IIterator<T> fork() {
		return new ArrayIterator<>(this);
	}
	
	@Override
	public T get0() {
		try {
			return array[i];
		} catch (ArrayIndexOutOfBoundsException exception) {
			throw new IllegalStateException(exception);
		}
	}
	
	@Override
	public boolean isValid() {
		return i < array.length;
	}
	
	@Override
	public IIterator<T> next() {
		check();
		i++;
		return this;
	}
	
}
