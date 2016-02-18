package com.g2forge.alexandria.java;

public class ArrayHelpers {
	@SafeVarargs
	public static <T> boolean contains(T value, T... array) {
		for (int i = 0; i < array.length; i++) {
			if (value.equals(array[i])) return true;
		}
		return false;
	}
}
