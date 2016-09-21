package com.g2forge.alexandria.test;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Tester<T> {
	@Getter(AccessLevel.PROTECTED)
	protected final T value;

	public <U> Tester<T> assertEquals(U expected) {
		return assertEquals(expected, Function.identity());
	}

	public <U, I> Tester<T> assertEquals(U expected, BiFunction<? super T, ? super I, ? extends U> function, I argument) {
		return assertTest(AssertHelpers.testEquals(expected), function, argument);
	}

	public <U> Tester<T> assertEquals(U expected, Function<? super T, ? extends U> function) {
		return assertTest(AssertHelpers.testEquals(expected), function);
	}

	public <U> Tester<T> assertInstanceOf(Class<?> expected) {
		return assertInstanceOf(expected, Function.identity());
	}

	public <U, I> Tester<T> assertInstanceOf(Class<?> expected, BiFunction<? super T, ? super I, ? extends U> function, I argument) {
		return assertTest(AssertHelpers.testInstanceOf(expected), function, argument);
	}

	public <U> Tester<T> assertInstanceOf(Class<?> expected, Function<? super T, ? extends U> function) {
		return assertTest(AssertHelpers.testInstanceOf(expected), function);
	}

	public <U> Tester<T> assertTest(Consumer<? super T> tester) {
		tester.accept(getValue());
		return this;
	}

	public <U, I> Tester<T> assertTest(Consumer<? super U> tester, BiFunction<? super T, ? super I, ? extends U> function, I argument) {
		final U actual = function.apply(getValue(), argument);
		tester.accept(actual);
		return this;
	}

	public <U> Tester<T> assertTest(Consumer<? super U> tester, Function<? super T, ? extends U> function) {
		final U actual = function.apply(getValue());
		tester.accept(actual);
		return this;
	}
}
