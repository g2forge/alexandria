package com.g2forge.alexandria.java.nestedstate;

import java.util.Stack;
import java.util.function.Function;

import com.g2forge.alexandria.java.close.ICloseable;

public class StackGlobalState<T> implements ICloseableNestedState<T> {
	protected final Stack<T> stack = new Stack<>();

	public StackGlobalState() {}

	public StackGlobalState(T initial) {
		stack.push(initial);
	}

	public void close(final T expected) {
		if (stack.peek() != expected) throw new IllegalStateException();
		stack.pop();
	}

	public int depth() {
		return stack.size();
	}

	@Override
	public T get() {
		return stack.peek();
	}

	public ICloseable modify(Function<? super T, ? extends T> function) {
		final T value = function.apply(stack.peek());
		stack.push(value);
		return () -> close(value);
	}

	@Override
	public ICloseable open(T value) {
		stack.push(value);
		return () -> close(value);
	}
}
