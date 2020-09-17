package com.g2forge.alexandria.regex;

import com.g2forge.alexandria.java.function.IFunction1;

@FunctionalInterface
public interface IConvertingBuilder<I, O> {
	public default IFunction1<I, O> asFunction() {
		return this::build;
	}

	public O build(I input);
}
