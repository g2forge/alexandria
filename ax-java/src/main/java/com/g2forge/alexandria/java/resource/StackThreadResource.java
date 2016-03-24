package com.g2forge.alexandria.java.resource;

import java.util.Stack;
import java.util.function.Function;

import com.g2forge.alexandria.java.close.ICloseable;

public class StackThreadResource<T> implements IThreadResource<T> {
	protected final ThreadLocal<Stack<T>> local = ThreadLocal.withInitial(Stack::new);

	@Override
	public T get() {
		return local.get().peek();
	}

	public ICloseable modify(Function<? super T, ? extends T> function) {
		final Stack<T> stack = local.get();
		final T value = function.apply(stack.peek());
		stack.push(value);
		return () -> {
			if (stack.pop() != value) throw new IllegalStateException();
		};
	}

	@Override
	public ICloseable open(T value) {
		final Stack<T> stack = local.get();
		stack.push(value);
		return () -> {
			if (stack.pop() != value) throw new IllegalStateException();
		};
	}
}
