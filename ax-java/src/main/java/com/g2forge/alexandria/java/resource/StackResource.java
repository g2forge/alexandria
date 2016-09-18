package com.g2forge.alexandria.java.resource;

import java.util.Stack;
import java.util.function.Function;

import com.g2forge.alexandria.java.close.ICloseable;

public class StackResource<T> implements IThreadResource<T> {
	protected final Stack<T> stack = new Stack<>();

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
		return () -> {
			if (stack.pop() != value) throw new IllegalStateException();
		};
	}

	@Override
	public ICloseable open(T value) {
		stack.push(value);
		return () -> {
			if (stack.pop() != value) throw new IllegalStateException();
		};
	}
}
