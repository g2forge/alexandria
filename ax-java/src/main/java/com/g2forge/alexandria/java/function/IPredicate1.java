package com.g2forge.alexandria.java.function;

import java.util.function.Predicate;

@FunctionalInterface
public interface IPredicate1<T> extends Predicate<T>, IPredicate {
	public static <T> IPredicate1<T> create(boolean value) {
		return t -> value;
	}

	public static <T> IPredicate1<T> create(IPredicate1<T> predicate) {
		return predicate;
	}
}
