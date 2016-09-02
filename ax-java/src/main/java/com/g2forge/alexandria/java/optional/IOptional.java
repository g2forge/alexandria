package com.g2forge.alexandria.java.optional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface IOptional<T> {
	public IOptional<T> filter(Predicate<? super T> predicate);

	public <U> IOptional<U> flatMap(Function<? super T, IOptional<U>> mapper);

	public T get();

	public void ifPresent(Consumer<? super T> consumer);

	public boolean isPresent();

	public <U> IOptional<U> map(Function<? super T, ? extends U> mapper);

	public T orElse(T other);

	public T orElseGet(Supplier<? extends T> other);

	public <X extends Throwable> T orElseThrow(Supplier<? extends X> exception) throws X;
}
