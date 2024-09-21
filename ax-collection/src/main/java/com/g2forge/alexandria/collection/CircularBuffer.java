package com.g2forge.alexandria.collection;

import java.util.ArrayList;
import java.util.List;

public class CircularBuffer<T> {
	protected final Object[] array;

	protected int head = 0;

	protected boolean full = false;

	public CircularBuffer(int size) {
		array = new Object[size];
	}

	public void add(T value) {
		array[head++] = value;
		if (head >= array.length) {
			full = true;
			head = 0;
		}
	}

	public List<T> getList() {
		final int count = full ? array.length : head;
		final int tail = full ? head : 0;
		final List<T> retVal = new ArrayList<T>(count);
		for (int i = 0; i < count; i++) {
			@SuppressWarnings("unchecked")
			final T cast = (T) array[rollover(i + tail)];
			retVal.add(cast);
		}
		return retVal;
	}

	protected int rollover(int index) {
		final int retVal = index % array.length;
		if (retVal < 0) return retVal + array.length;
		return retVal;
	}
}
