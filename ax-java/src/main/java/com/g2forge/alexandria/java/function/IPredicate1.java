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

	public default <IX> IPredicate2<IX, T> ignore0() {
		return (i0, i1) -> test(i1);
	}

	public default <IX> IPredicate2<T, IX> ignore1() {
		return (i0, i1) -> test(i0);
	}

	public default IPredicate1<T> negate() {
		return i -> !test(i);
	}
}
