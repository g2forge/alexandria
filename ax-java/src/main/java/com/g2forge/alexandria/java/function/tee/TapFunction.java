package com.g2forge.alexandria.java.function.tee;

import java.util.function.Consumer;

import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TapFunction<T> implements IFunction1<T, T> {
	protected final Consumer<? super T> consumer;

	@Override
	public T apply(T input) {
		consumer.accept(input);
		return input;
	}
}
