package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IFunction1;

public interface ISerializableFunction1<I, O> extends IFunction1<I, O>, SerializableLambda, ISerializableFunction<O> {
	public static <I, O> IMethodAnalyzer analyze(ISerializableFunction1<I, O> lambda) {
		return new MethodAnalyzer(lambda);
	}
	
	public static <I, O> ISerializableFunction1<I, O> create(ISerializableFunction1<I, O> function) {
		return function;
	}
}