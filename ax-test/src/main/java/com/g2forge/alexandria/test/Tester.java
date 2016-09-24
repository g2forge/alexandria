package com.g2forge.alexandria.test;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.g2forge.alexandria.java.function.IBiFunction;
import com.g2forge.alexandria.java.function.IFunction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Tester<T> {
	@Getter
	protected final T value;

	public <U> Tester<T> assertEquals(U expected) {
		return assertEquals(expected, IFunction.identity());
	}

	public <U, I> Tester<T> assertEquals(U expected, IBiFunction<? super T, ? super I, ? extends U> function, I argument) {
		return assertTest(AssertHelpers.testEquals(expected), function, argument);
	}

	public <U> Tester<T> assertEquals(U expected, IFunction<? super T, ? extends U> function) {
		return assertTest(AssertHelpers.testEquals(expected), function);
	}

	public <U, I> Tester<T> assertException(Class<? extends Throwable> type, IBiFunction<? super T, ? super I, ? extends U> function, I argument) {
		return assertExec(s -> AssertHelpers.assertException(type, s::get), function, argument);
	}

	public <U> Tester<T> assertException(Class<? extends Throwable> type, IFunction<? super T, ? extends U> function) {
		return assertExec(s -> AssertHelpers.assertException(type, s::get), function);
	}

	public <U> Tester<T> assertExec(Consumer<? super Supplier<? extends T>> executor) {
		final Supplier<? extends T> supplier = this::getValue;
		executor.accept(supplier);
		return this;
	}

	public <U, I> Tester<T> assertExec(Consumer<? super Supplier<? extends U>> executor, IBiFunction<? super T, ? super I, ? extends U> function, I argument) {
		final Supplier<? extends U> supplier = function.compose0(this::getValue).curry(argument);
		executor.accept(supplier);
		return this;
	}

	public <U> Tester<T> assertExec(Consumer<? super Supplier<? extends U>> executor, IFunction<? super T, ? extends U> function) {
		final Supplier<? extends U> supplier = function.compose(this::getValue);
		executor.accept(supplier);
		return this;
	}

	public <U> Tester<T> assertInstanceOf(Class<?> expected) {
		return assertInstanceOf(expected, IFunction.identity());
	}

	public <U, I> Tester<T> assertInstanceOf(Class<?> expected, IBiFunction<? super T, ? super I, ? extends U> function, I argument) {
		return assertTest(AssertHelpers.testInstanceOf(expected), function, argument);
	}

	public <U> Tester<T> assertInstanceOf(Class<?> expected, IFunction<? super T, ? extends U> function) {
		return assertTest(AssertHelpers.testInstanceOf(expected), function);
	}

	public <U> Tester<T> assertTest(Consumer<? super T> tester) {
		return assertExec(s -> tester.accept(s.get()));
	}

	public <U, I> Tester<T> assertTest(Consumer<? super U> tester, IBiFunction<? super T, ? super I, ? extends U> function, I argument) {
		return assertExec(s -> tester.accept(s.get()), function, argument);
	}

	public <U> Tester<T> assertTest(Consumer<? super U> tester, IFunction<? super T, ? extends U> function) {
		return assertExec(s -> tester.accept(s.get()), function);
	}
}