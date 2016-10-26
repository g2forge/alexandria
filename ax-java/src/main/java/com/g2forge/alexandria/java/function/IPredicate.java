package com.g2forge.alexandria.java.function;

import java.util.function.Predicate;

@FunctionalInterface
public interface IPredicate<T> extends Predicate<T> {
	public static <T> IPredicate<T> create(boolean value) {
		return t -> value;
	}

	public static <T> IPredicate<T> create(IPredicate<T> predicate) {
		return predicate;
	}
}
