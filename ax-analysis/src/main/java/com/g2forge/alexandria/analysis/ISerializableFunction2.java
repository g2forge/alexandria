package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IFunction2;

public interface ISerializableFunction2<I0, I1, O> extends IFunction2<I0, I1, O>, SerializableLambda, ISerializableFunction<O> {
	public static <I0, I1, O> IMethodAnalyzer analyze(ISerializableFunction2<I0, I1, O> lambda) {
		return new MethodAnalyzer(lambda);
	}
	
	public static <I0, I1, O> ISerializableFunction2<I0, I1, O> create(ISerializableFunction2<I0, I1, O> function) {
		return function;
	}
}