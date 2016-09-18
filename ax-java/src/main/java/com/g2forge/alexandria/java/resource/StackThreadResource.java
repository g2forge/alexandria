package com.g2forge.alexandria.java.resource;

import java.util.Stack;
import java.util.function.Function;
import java.util.function.Supplier;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.LiteralSupplier;

public class StackThreadResource<T> implements ICloseableResource<T> {
	protected final ThreadLocal<Stack<T>> local;

	public StackThreadResource() {
		this.local = ThreadLocal.withInitial(Stack::new);
	}

	public StackThreadResource(Supplier<? extends T> initial) {
		this.local = ThreadLocal.withInitial(() -> {
			final Stack<T> stack = new Stack<>();
			stack.add(initial.get());
			return stack;
		});
	}

	public StackThreadResource(T initial) {
		this(new LiteralSupplier<>(initial));
	}

	public int depth() {
		return local.get().size();
	}

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
