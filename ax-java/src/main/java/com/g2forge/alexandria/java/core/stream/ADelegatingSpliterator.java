package com.g2forge.alexandria.java.core.stream;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

import com.g2forge.alexandria.java.function.ISupplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ADelegatingSpliterator<T, S extends Spliterator<T>> implements Spliterator<T> {
	protected final ISupplier<? extends S> supplier;

	protected S s;

	@Override
	public void forEachRemaining(Consumer<? super T> consumer) {
		get().forEachRemaining(consumer);
	}

	protected S get() {
		if (s == null) s = supplier.get();
		return s;
	}

	@Override
	public Comparator<? super T> getComparator() {
		return get().getComparator();
	}

	@Override
	public String toString() {
		return getClass().getName() + "[" + supplier + "]";
	}

	@Override
	public boolean tryAdvance(Consumer<? super T> consumer) {
		return get().tryAdvance(consumer);
	}

	@Override
	@SuppressWarnings("unchecked")
	public S trySplit() {
		return (S) get().trySplit();
	}
}