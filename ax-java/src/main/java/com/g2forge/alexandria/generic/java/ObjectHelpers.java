package com.g2forge.alexandria.generic.java;

public class ObjectHelpers {
	protected static final int HASHCODE_PRIME = 31;

	public static boolean equals(final Object left, final Object right) {
		if ((left == null) && (right == null)) return true;
		if ((left == null) || (right == null)) return false;
		return left.equals(right);
	}

	public static int hashCode(final Object... objects) {
		int result = 1;
		for (final Object object : objects)
			result = (HASHCODE_PRIME * result) + hashCode(object);
		return result;
	}

	public static int hashCode(final Object object) {
		return (object == null) ? 0 : object.hashCode();
	}

	public static String toString(final Object object) {
		return (object == null) ? "null" : object.toString();
	}
}
