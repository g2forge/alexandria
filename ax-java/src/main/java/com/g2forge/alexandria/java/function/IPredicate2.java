package com.g2forge.alexandria.java.function;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface IPredicate2<I0, I1> extends BiPredicate<I0, I1>, IPredicate {
	public static <I0, I1> IPredicate2<I0, I1> create(boolean value) {
		return (i0, i1) -> value;
	}

	public static <I0, I1> IPredicate2<I0, I1> create(IPredicate2<I0, I1> predicate) {
		return predicate;
	}

	public default IPredicate1<I1> compose0(Supplier<? extends I0> before) {
		Objects.requireNonNull(before);
		return i1 -> test(before.get(), i1);
	}

	public default IPredicate1<I0> compose1(Supplier<? extends I1> before) {
		Objects.requireNonNull(before);
		return i0 -> test(i0, before.get());
	}

	public default IPredicate1<I1> curry0(I0 input0) {
		return input1 -> test(input0, input1);
	}

	public default IPredicate1<I0> curry1(I1 input1) {
		return input0 -> test(input0, input1);
	}

	public default <IX> IPredicate3<IX, I0, I1> ignore0() {
		return (i0, i1, i2) -> test(i1, i2);
	}

	public default <IX> IPredicate3<I0, IX, I1> ignore1() {
		return (i0, i1, i2) -> test(i0, i2);
	}

	public default <IX> IPredicate3<I0, I1, IX> ignore2() {
		return (i0, i1, i2) -> test(i0, i1);
	}

	public default IPredicate2<I0, I1> negate() {
		return (i0, i1) -> !test(i0, i1);
	}

	public default IConsumer2<I0, I1> noReturn() {
		return (i0, i1) -> test(i0, i1);
	}
}
