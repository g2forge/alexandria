package com.g2forge.alexandria.java.nestedstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Supplier;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.LiteralSupplier;

public class StackThreadState<T> implements INestedState<T>, ITransparentNestedState<T> {
	protected final ThreadLocal<Stack<T>> local;

	public StackThreadState() {
		this.local = ThreadLocal.withInitial(Stack::new);
	}

	public StackThreadState(Supplier<? extends T> initial) {
		this.local = ThreadLocal.withInitial(() -> {
			final Stack<T> stack = new Stack<>();
			stack.add(initial.get());
			return stack;
		});
	}

	public StackThreadState(T initial) {
		this(new LiteralSupplier<>(initial));
	}

	protected void close(final Stack<T> expectedStack, final T expectedValue) {
		if (expectedStack != local.get()) throw new IllegalStateException("Closed from the wrong thread!");
		if (expectedStack.pop() != expectedValue) throw new IllegalStateException();
	}

	@Override
	public int depth() {
		return local.get().size();
	}
	
	@Override
	public List<T> getCurrent() {
		return new ArrayList<>(local.get());
	}

	@Override
	public T get() {
		return local.get().peek();
	}

	public ICloseable modify(Function<? super T, ? extends T> function) {
		final Stack<T> stack = local.get();
		final T value = function.apply(stack.peek());
		stack.push(value);
		return () -> close(stack, value);
	}

	@Override
	public ICloseable open(T value) {
		final Stack<T> stack = local.get();
		stack.push(value);
		return () -> close(stack, value);
	}
}
