package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface IPredicate1<T> extends Predicate<T>, IPredicate {
	public static <T> IPredicate1<T> create(boolean value) {
		return t -> value;
	}

	public static <T> IPredicate1<T> create(IPredicate1<T> predicate) {
		return predicate;
	}

	public static <T> IPredicate1<T> isEqual(T value) {
		return new IsEqualPredicate1<>(value);
	}

	public default IPredicate1<T> and(Predicate<? super T> other) {
		Objects.requireNonNull(other);
		return i -> test(i) && other.test(i);
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

	public default IPredicate1<T> or(Predicate<? super T> other) {
		Objects.requireNonNull(other);
		return i -> test(i) || other.test(i);
	}
}
