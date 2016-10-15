package com.g2forge.alexandria.java.function;

import java.util.function.Predicate;

@FunctionalInterface
public interface IPredicate<I> extends Predicate<I> {
	public static <T> IPredicate<T> create(boolean value) {
		return t -> value;
	}
}
