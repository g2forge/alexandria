package com.g2forge.alexandria.analysis;

import java.util.function.Function;

public interface SerializableFunction<I, O> extends Function<I, O>, SerializableLambda {
	public static <I, O> IMethodAnalyzer analyze(SerializableFunction<I, O> lambda) {
		return new MethodAnalyzer(lambda);
	}
}